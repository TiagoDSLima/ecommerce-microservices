#!/bin/bash
# Popula o ambiente local com dados de teste, passando pelo api-gateway.
# Pré-requisitos: containers de servicos/ (database, broker, bucket) e os 6 microsserviços no ar.
# Uso: bash servicos/seed/seed.sh
# Pode ser executado mais de uma vez: usuários existentes só fazem login e produtos só são criados se não houver nenhum.

set -euo pipefail

GATEWAY=${GATEWAY:-http://localhost:8080}
DB_CONTAINER=${DB_CONTAINER:-db_ecommerce}
DIR="$(cd "$(dirname "$0")" && pwd)"

# Credenciais de teste (apenas ambiente local)
ADMIN_EMAIL=admin@ecommerce.test
ADMIN_SENHA=admin123
CLIENTE_EMAIL=cliente@ecommerce.test
CLIENTE_SENHA=cliente123

json_campo() { sed -n "s/.*\"$1\":\"\{0,1\}\([^\",}]*\).*/\1/p"; }

registra_ou_loga() { # email senha nome cpf
    local resp
    resp=$(curl -s -X POST "$GATEWAY/auth/register" -H 'Content-Type: application/json' \
        -d "{\"email\":\"$1\",\"senha\":\"$2\",\"nome\":\"$3\",\"cpf\":\"$4\",\"dataNascimento\":\"1995-05-10\"}")
    if ! echo "$resp" | grep -q '"token"'; then
        resp=$(curl -s -X POST "$GATEWAY/auth/login" -H 'Content-Type: application/json' \
            -d "{\"email\":\"$1\",\"senha\":\"$2\"}")
    fi
    echo "$resp"
}

echo "==> Usuários"
registra_ou_loga "$ADMIN_EMAIL" "$ADMIN_SENHA" "Administrador" "11144477735" > /dev/null
registra_ou_loga "$CLIENTE_EMAIL" "$CLIENTE_SENHA" "Maria Silva" "52998224725" > /dev/null

# Não existe endpoint para promover usuário: o admin é definido direto no banco
docker exec "$DB_CONTAINER" psql -U postgres -d ecommerceauth -q -c \
    "UPDATE usuario SET role = 'ROLE_ADMIN' WHERE email = '$ADMIN_EMAIL';"

ADMIN_TOKEN=$(registra_ou_loga "$ADMIN_EMAIL" "$ADMIN_SENHA" "" "" | json_campo token)
echo "    admin:   $ADMIN_EMAIL (ROLE_ADMIN)"
echo "    cliente: $CLIENTE_EMAIL (ROLE_USER)"

echo "==> Produtos"
if [ "$(curl -s "$GATEWAY/produto/buscaTodos")" = "[]" ]; then
    cd "$DIR"  # caminho relativo: o curl nativo do Windows não entende /c/...
    IMG=produto.png
    cria_produto() {
        # A descrição vai por arquivo (<) para manter os bytes UTF-8: o curl nativo do Windows
        # converte os argumentos para outra codificação e corromperia os acentos
        printf '%s' "$1" > descricao.tmp
        curl -s -o /dev/null -w "    %{http_code} $1\n" -X POST "$GATEWAY/produto" -H "Authorization: Bearer $ADMIN_TOKEN" \
            -F "descricao=<descricao.tmp;type=text/plain;charset=UTF-8" -F "valorUnitario=$2" -F "quantidade=$3" \
            -F "imagem=@$IMG;type=image/png" "${@:4}"
        rm -f descricao.tmp
    }
    cria_produto "Caneca Personalizada" 39.90 50
    cria_produto "Camiseta Básica" 59.90 30 \
        -F "variacoes[0].tamanho=P" -F "variacoes[0].cor=Preta" -F "variacoes[0].quantidade=10" \
        -F "variacoes[1].tamanho=M" -F "variacoes[1].cor=Preta" -F "variacoes[1].quantidade=10" \
        -F "variacoes[2].tamanho=G" -F "variacoes[2].cor=Branca" -F "variacoes[2].quantidade=10"
    cria_produto "Boné Aba Curva" 49.90 20
else
    echo "    produtos já cadastrados, nada a fazer"
fi

echo "==> Participante do cliente (criado via Kafka a partir do auth)"
for _ in $(seq 1 15); do
    total=$(docker exec "$DB_CONTAINER" psql -U postgres -d ecommerceparticipante -tA -c "SELECT count(*) FROM participante;")
    [ "$total" -ge 2 ] && break
    sleep 1
done
echo "    participantes cadastrados: $total"

echo "Pronto."

# Imagem base para Node.js
FROM node:20-alpine
WORKDIR /usr/src/app

# Copia o manifesto de dependências e instala
COPY package.json .
RUN npm install

# Copia o código do Gateway
COPY gateway.js .

# A porta do Gateway é 3000 (apiGateway/gateway.js)
EXPOSE 3000

# Comando para iniciar a aplicação
CMD ["node", "gateway.js"]
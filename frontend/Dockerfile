# --------------------------------------------------
# ESTÁGIO 1: Build (Compilação do Angular)
# --------------------------------------------------
FROM node:20-alpine AS build
WORKDIR /app

# Instala as dependências
COPY package.json .
COPY angular.json .
RUN npm install

# Copia o resto do código e compila para produção
COPY . .
# O comando de build do Angular para produção (o output vai para dist/banco/browser)
RUN npm run build -- --configuration production

# --------------------------------------------------
# ESTÁGIO 2: Runtime (Servindo com Nginx)
# --------------------------------------------------
FROM nginx:alpine
# Copia o output da build para o diretório padrão do Nginx
COPY --from=build /app/dist/banco/browser /usr/share/nginx/html
# A porta padrão para o tráfego web
EXPOSE 80
# Comando para rodar o Nginx em foreground
CMD ["nginx", "-g", "daemon off;"]
const express = require('express');
const { createProxyMiddleware } = require('http-proxy-middleware');
const morgan = require('morgan');
const cors = require('cors');

const app = express();
const PORT = 3000;

app.use(morgan('dev'));
app.use(cors());

// LENDO VARIÁVEIS DE AMBIENTE INJETADAS PELO DOCKER COMPOSE
// Se a variável não estiver definida (fallback para localhost para testes locais)
const MS_CLIENTE_TARGET = `http://${process.env.MS_CLIENTE_HOST || 'localhost'}:8080`;
const MS_AUTH_TARGET = `http://${process.env.MS_AUTH_HOST || 'localhost'}:8081`;
const MS_GERENTE_TARGET = `http://${process.env.MS_GERENTE_HOST || 'localhost'}:8082`;


// Rota para o microserviço de clientes
app.use('/api/clientes', createProxyMiddleware({
  target: MS_CLIENTE_TARGET, // Usa a variável
  changeOrigin: true,
  pathRewrite: {
    '^/api': '', 
  },
}));

// Rota para o microserviço de autenticação
app.use('/auth', createProxyMiddleware({
  target: MS_AUTH_TARGET, // Usa a variável
  changeOrigin: true,
  logLevel: 'debug',   
}));

// Rota para o microserviço de gerentes
app.use('/gerentes', createProxyMiddleware({ 
  target: MS_GERENTE_TARGET, // Usa a variável
  changeOrigin: true,
}));

// 🔹 Rota principal (teste)
app.get('/', (req, res) => {
  res.send('Gateway ativo e roteando requisições!');
});

app.listen(PORT, () => {
  console.log(`🚀 API Gateway rodando em http://localhost:${PORT}`);
});
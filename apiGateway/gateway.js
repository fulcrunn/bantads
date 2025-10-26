const express = require('express');
const { createProxyMiddleware } = require('http-proxy-middleware');
const morgan = require('morgan');
const cors = require('cors');

const app = express();
const PORT = 3000;

app.use(morgan('dev'));
app.use(cors());

// Rota para o microserviço de clientes
app.use('/api/clientes', createProxyMiddleware({
  target: 'http://localhost:8080/api/clientes',
  changeOrigin: true,
  pathRewrite: {
    '^/api': '', 
  },
  //pathRewrite: { '^/api/clientes': '' },
}));

// Rota para o microserviço de autenticação
app.use('/auth', createProxyMiddleware({
  target: 'http://localhost:8081/auth',
  changeOrigin: true,
  logLevel: 'debug',   
}));

// Rota para o microserviço de gerentes
app.use('/gerentes', createProxyMiddleware({ // Escuta por pedidos em /gerentes/...
  target: 'http://localhost:8082/gerentes',       // Encaminha para ms-gerente (porta 8082)
  changeOrigin: true,
  // pathRewrite não é necessário aqui, pois o ms-gerente também espera /gerentes/...
}));

// 🔹 Rota principal (teste)
app.get('/', (req, res) => {
  res.send('Gateway ativo e roteando requisições!');
});

app.listen(PORT, () => {
  console.log(`🚀 API Gateway rodando em http://localhost:${PORT}`);
});

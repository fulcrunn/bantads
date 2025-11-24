const express = require('express');
const { createProxyMiddleware } = require('http-proxy-middleware');
const morgan = require('morgan');
const cors = require('cors');

const app = express();
const PORT = 3000;

app.use(morgan('dev'));
app.use(cors());

// Configuração dos Hosts (apenas domínio e porta)
const MS_CLIENTE_HOST = `http://${process.env.MS_CLIENTE_HOST || 'localhost'}:8080`;
const MS_AUTH_HOST = `http://${process.env.MS_AUTH_HOST || 'localhost'}:8081`;
const MS_GERENTE_HOST = `http://${process.env.MS_GERENTE_HOST || 'localhost'}:8082`;
const MS_CONTA_HOST = `http://${process.env.MS_CONTA_HOST || 'localhost'}:8084`;

// Rota Clientes
app.use('/api/clientes', createProxyMiddleware({
  // Adiciona '/api/clientes' porque o app.use remove e o backend espera receber
  target: `${MS_CLIENTE_HOST}/api/clientes`, 
  changeOrigin: true,
}));

// Rota Auth
app.use('/auth', createProxyMiddleware({
  // Adiciona '/auth' porque o backend espera /auth/login
  target: `${MS_AUTH_HOST}/auth`, 
  changeOrigin: true,
  logLevel: 'debug',   
}));

// Rota Gerentes
app.use('/gerentes', createProxyMiddleware({ 
  // Adiciona '/gerentes'
  target: `${MS_GERENTE_HOST}/gerentes`,
  changeOrigin: true,
}));

app.use('/contas', createProxyMiddleware({ 
  // Acesso a ms-conta
  target: `${MS_CONTA_HOST}/contas`,
  changeOrigin: true,
}));

app.get('/', (req, res) => {
  res.send('Gateway ativo e roteando requisições!');
});

app.listen(PORT, () => {
  console.log(`🚀 API Gateway rodando em http://localhost:${PORT}`);

});
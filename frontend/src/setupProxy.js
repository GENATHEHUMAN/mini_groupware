const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function(app) {
  app.use(
    '/api', // '/api'로 시작하는 모든 요청을 프록시 처리합니다.
    createProxyMiddleware({
      target: 'http://localhost:8080', // 백엔드 서버 주소
      changeOrigin: true,
    })
  );
};
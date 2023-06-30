const swaggerOptions = {
    swaggerDefinition: {
        info: {
            title: 'My API',
            version: '1.0.0',
            description: 'My API Documentation'
        },
        servers: [{ "url": "http://localhost:8001" }],
        securityDefinitions: {
            Bearer: {
              type: 'apiKey',
              name: 'Authorization',
              scheme: 'bearer',
              in: 'header',
            },
        },
        security: [{
            Bearer: []
        }], // This line needs to be added
    },
    apis: ['src/routes/*.js']
};

export default swaggerOptions;
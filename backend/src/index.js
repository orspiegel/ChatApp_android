import express from 'express';
import mongoose from 'mongoose';
import dotenv from 'dotenv';
import cors from 'cors';
import helmet from 'helmet';
import http from 'http';
import { Server } from 'socket.io';


// Import routes
import chatRoutes from './routes/chatRoutes.js';
import userRoutes from './routes/userRoutes.js';
import tokenRoutes from './routes/index.js';
import swaggerUi from 'swagger-ui-express';
import swaggerJsdoc from 'swagger-jsdoc';
import swaggerOptions from './swaggerOptions.js';
dotenv.config();

const app = express();

//app.use(helmet()); // helps you secure your Express apps by setting various HTTP headers
app.use(cors()); // allows you to configure CORS

const server = http.createServer(app);
const io = new Server(server, {
  cors: {
    origin: "http://localhost:3000",
    methods: ["GET", "POST"]
  }
});

app.use(express.json({ limit: '50mb' }));
app.use(express.urlencoded({ limit: '50mb', extended: true }));const swaggerDocs = swaggerJsdoc(swaggerOptions);

app.use('/api-docs', swaggerUi.serve, swaggerUi.setup(swaggerDocs));
// Connect to MongoDB
mongoose.connect(process.env.DATABASE_URL, { useNewUrlParser: true, useUnifiedTopology: true })
    .then(() => console.log("MongoDB connected..."))
    .catch(err => console.log(err));

// Define routes
app.use('/api/Chats', chatRoutes);
app.use('/api/Users', userRoutes);
app.use('/api', tokenRoutes);


app.use((err, req, res, next) => { // error handling middleware
  console.error(err.stack);
  res.status(500).send('Something broke!');
});

io.on("connection", (socket) => {

  socket.on("join_room", (data) => {
    if (data) {
    console.log("attempting join server side... " + data);
    socket.join(data);
    }
  });

/*If receives Firebase header, push and promt a notification on the other end*/

  socket.on("send_message", (data) => {
  //console.log(data.msg);
  //console.log(data.room);
  console.log("msg sent: ", data.msg.content, "sender ID: ", data.msg.sender, "timestamp: ", data.msg.created);

  socket.to(data.room).emit("receive_message", data);
  });
});

const PORT = process.env.PORT || 5000;
server.listen(PORT, () => console.log(`Server is running on port ${PORT}`));

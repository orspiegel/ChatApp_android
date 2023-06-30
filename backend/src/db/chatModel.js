import mongoose from 'mongoose';

const { Schema } = mongoose;

const messageSchema = new Schema({
    sender: { type: Schema.Types.ObjectId, ref: 'User' },
    content: String,
    created: { type: Date, default: Date.now }
});

const chatSchema = new Schema({
    users: [{ type: Schema.Types.ObjectId, ref: 'User' }],
    messages: [messageSchema]
});

const Chat = mongoose.model('Chat', chatSchema);

export default Chat;
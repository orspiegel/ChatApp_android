import mongoose from 'mongoose';

const { Schema } = mongoose;

const userSchema = new Schema({
    username: { type: String, required: true },
    password: { type: String, required: true },
    displayName: { type: String, required: true },
    profilePic: { type: String, default: '' }
});

const User = mongoose.model('User', userSchema);

export default User;
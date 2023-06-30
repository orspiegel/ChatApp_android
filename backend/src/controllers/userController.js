import bcrypt from 'bcryptjs';
import jwt from 'jsonwebtoken';
import User from '../db/userModel.js';

export const getUser = async (req, res) => {
    console.log("Requested username:", req.params.username); // This line is for debugging
    const user = await User.findOne({ username: req.params.username }).select('-password');
    if (!user) {
        console.log("User not found in the database"); // This line is for debugging
        return res.status(404).send('User not found');
    }
    res.send(user);
};

export const createUser = async (req, res) => {
    // Check if user already exists
    let user = await User.findOne({ username: req.body.username });
    if (user) return res.status(409).send('User already exists');

    // Hash the password
    const salt = await bcrypt.genSalt(10);
    const hashedPassword = await bcrypt.hash(req.body.password, salt);

    // Create new user
    user = new User({
        username: req.body.username,
        password: hashedPassword,
        displayName: req.body.displayName,
        profilePic: req.body.profilePic,
    });
    await user.save();

    res.send(user);
};

export const loginUser = async (req, res) => {
    // Check if user exists
    const user = await User.findOne({ username: req.body.username });
    if (!user) return res.status(404).send('Invalid email or password');

    // Check if password is correct
    const validPassword = await bcrypt.compare(req.body.password, user.password);
    if (!validPassword) return res.status(404).send('Invalid email or password');

    // Create and assign a token
    const token = jwt.sign({ _id: user._id }, process.env.JWT_SECRET);
    res.header('Authorization', token).send(token);
};
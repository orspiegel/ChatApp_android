import Chat from '../db/chatModel.js';
import User from '../db/userModel.js';

export const getChats = async (req, res) => {
    const userId = req.user._id;
    const chats = await Chat.find({ users: userId })
        .populate('users', '-password')
        .populate('messages.sender', '-password');

    // Create a new array of modified chat objects
    const modifiedChats = await Promise.all(chats.map(async (chat) => {
        const otherUserId = chat.users.find(u => u._id.toString() !== userId.toString());
        if (!otherUserId) return null;

        const otherUser = await User.findById(otherUserId);
        let lastMessage = chat.messages[chat.messages.length - 1];

        // Check if there is no last message and assign a default value if so
        if (!lastMessage) {
            lastMessage = {
                _id: null,
                created: null,
                content: null
            };
        }

        return {
            id: chat._id,
            user: {
                username: otherUser.username,
                displayName: otherUser.displayName,
                profilePic: otherUser.profilePic
            },
            lastMessage: {
                id: lastMessage._id,
                created: lastMessage.created,
                content: lastMessage.content
            }
        };
    }));

    // Filter out any null chats (in case there were chats with only one user)
    const filteredChats = modifiedChats.filter(chat => chat !== null);

    // Sort based on the most recent message sent.
    const sortedChats = filteredChats.sort((chat1, chat2) => chat2.lastMessage.created - chat1.lastMessage.created);

    res.send(sortedChats);
};

export const getChat = async (req, res) => {
    const chat = await Chat.findById(req.params.id).populate('users', '-password').populate('messages.sender', '-password');
    if (!chat) return res.status(404).send('Chat not found');
    res.send(chat);
};

export const createChat = async (req, res) => {
    const user = await User.findById(req.user._id); // Get the user from the JWT
    if (!user) return res.status(400).send('User not found');

    const otherUser = await User.findOne({ username: req.body.username }); // Get the other user
    if (!otherUser) return res.status(400).send('User not found');

    // Check if users are the same
    if (user.username === otherUser.username) {
        return res.status(400).send('Cannot create a chat with yourself');
    }

    const existingChat = await Chat.findOne({
        users: { $all: [user, otherUser] }
    });

    if (existingChat) {
        return res.status(400).send('Chat with this user already exists');
    }

    // Create the chat with the two users
    const chat = new Chat({ users: [user, otherUser] });
    await chat.save();

    const response = {
        id: chat._id,
        user: {
            username: otherUser.username,
            displayName: otherUser.displayName,
            profilePic: otherUser.profilePic
        }
    }
    res.send(response);
};


export const deleteChat = async (req, res) => {
    const chat = await Chat.findByIdAndDelete(req.params.id);
    if (!chat) return res.status(404).send('Chat not found');
    res.send(chat);
};

export const createMessage = async (req, res) => {
    const chat = await Chat.findById(req.params.id);
    if (!chat) return res.status(404).send('Chat not found');
    const user = await User.findById(req.user._id); 
    if (!user) return res.status(404).send('User not found');
    chat.messages.push({
        sender: user._id,
        content: req.body.msg
    });
    await chat.save();

    


    res.send(chat);
};

export const getMessages = async (req, res) => {
    const chat = await Chat.findById(req.params.id).populate('messages.sender', '-password');
    if (!chat) return res.status(404).send('Chat not found');

    const modifiedMessages = chat.messages.map((message) => {
        return {
            id: message._id,
            created: message.created,
            sender: {
                username: message.sender.username
            },
            content: message.content
        }
    });

    res.send(modifiedMessages);
};
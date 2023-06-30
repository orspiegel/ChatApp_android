import jwt from 'jsonwebtoken';

export const auth = async (req, res, next) => {
    try {
        console.log(req)
        const token = req.header('Authorization').replace('bearer ', '');
        if (!token) return res.status(401).send('Access Denied');

        const verified = jwt.verify(token, process.env.JWT_SECRET);
        if (!verified) return res.status(401).send('Invalid Token');

        req.user = verified;
        next();
    } catch (err) {
        res.status(400).send('Invalid Token');
    }
};
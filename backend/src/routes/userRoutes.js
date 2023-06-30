import express from 'express';
import {  getUser, createUser, loginUser } from '../controllers/userController.js';
import { auth } from '../middleware/auth.js';

const router = express.Router();

/**
 * @swagger
 * /api/Users:
 *   post:
 *     description: Create a user
 *     parameters:
 *       - in: body
 *         name: body
 *         required: true
 *         schema:
 *           type: object
 *           properties:
 *             username:
 *               type: string
 *               description: The user's username
 *             password:
 *               type: string
 *               description: The user's password
 *             displayName:
 *               type: string
 *               description: The user's display name
 *             profilePic:
 *               type: string
 *               description: URL of the user's profile picture
 *               default: ''
 *     responses:
 *       200:
 *         description: Success
 */
router.route('/')
    .post(createUser);  // no auth middleware here

/**
 * @swagger
 * /api/Users/{username}:
 *   get:
 *     description: Get a specific user
 *     parameters:
 *       - in: path
 *         name: username
 *         required: true
 *         schema:
 *           type: string
 *         description: The username.
 *     responses:
 *       200:
 *         description: Success
 */
router.route('/:username')
    .get(auth, getUser);

export default router;

import express from 'express';
import { getChats, getChat, createChat, deleteChat, createMessage, getMessages } from '../controllers/chatController.js';
import { auth } from '../middleware/auth.js';

const router = express.Router();

/**
 * @swagger
 * /api/Chats:
 *   get:
 *     security:
 *       - bearerAuth: []
 *     description: Get all chats
 *     responses:
 *       200:
 *         description: Success
 *   post:
 *     security:
 *       - bearerAuth: []
 *     description: Create a chat
 *     responses:
 *       200:
 *         description: Success
 */
router.route('/')
    .get(auth, getChats)
    .post(auth, createChat);

/**
 * @swagger
 * /api/Chats/{id}:
 *   get:
 *     security:
 *       - bearerAuth: []
 *     description: Get a specific chat
 *     parameters:
 *       - in: path
 *         name: id
 *         required: true
 *         schema:
 *           type: string
 *         description: The chat ID.
 *     responses:
 *       200:
 *         description: Success
 *   delete:
 *     security:
 *       - bearerAuth: []
 *     description: Delete a chat
 *     parameters:
 *       - in: path
 *         name: id
 *         required: true
 *         schema:
 *           type: string
 *         description: The chat ID.
 *     responses:
 *       200:
 *         description: Success
 */
router.route('/:id')
    .get(auth, getChat)
    .delete(auth, deleteChat);

/**
 * @swagger
 * /api/Chats/{id}/Messages:
 *   get:
 *     security:
 *       - bearerAuth: []
 *     description: Get messages for a specific chat
 *     responses:
 *       200:
 *         description: Success
 *   post:
 *     security:
 *       - bearerAuth: []
 *     description: Create a message for a specific chat
 *     responses:
 *       200:
 *         description: Success
 */
router.route('/:id/Messages')
    .get(auth, getMessages)
    .post(auth, createMessage);

export default router;

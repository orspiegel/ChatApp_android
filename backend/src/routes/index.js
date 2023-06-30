import express from 'express';
import { loginUser } from '../controllers/userController.js';
const router = express.Router();

/**
 * @swagger
 * /api/Tokens:
 *   post:
 *     description: Log in a user and get a token
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
 *     responses:
 *       200:
 *         description: Success
 */
 router.route('/Tokens')
 .post(loginUser);  // no auth middleware here

export default router;

package com.example.myapplication.Objects;

import com.example.myapplication.Entites.LastMessage;
    public class MessageResponse {
        String id;
        String content;
        String created;
        User sender;

        public MessageResponse(String id, String content, String created, User sender) {
            this.id = id;
            this.content = content;
            this.created = created;
            this.sender = sender;
        }

        public User getSender() {
            return sender;
        }

        public void setSender(User sender) {
            this.sender = sender;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }
    }
package com.example.myapplication.Objects;

import com.example.myapplication.Entites.LastMessage;
    public class MessageResponse {

        String sender;

        String content;
        String _id;
        String created;

        int _v;

        public MessageResponse(String id, String content, String created, String sender) {
            this._id = id;
            this.content = content;
            this.created = created;
            this.sender = sender;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public int get_v() {
            return _v;
        }

        public void set_v(int _v) {
            this._v = _v;
        }

        public String getSender() {
            return sender;
        }

        public void setSender(String sender) {
            this.sender = sender;
        }

        public String getId() {
            return _id;
        }

        public void setId(String id) {
            this._id = id;
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
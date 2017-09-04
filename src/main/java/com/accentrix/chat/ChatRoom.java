package com.accentrix.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.websocket.Session;

@Data
@NoArgsConstructor
@AllArgsConstructor
class ChatRoom {

    private String id;
    private Session session;

}

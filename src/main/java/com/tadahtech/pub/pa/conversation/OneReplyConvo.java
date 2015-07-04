package com.tadahtech.pub.pa.conversation;

import com.tadahtech.pub.pa.ProAnnouncer;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;

/**
 * Created by Timothy Andis
 */
public abstract class OneReplyConvo {

    private String prompt;
    protected Player player;

    public OneReplyConvo(String prompt, Player player) {
        this.prompt = prompt;
        this.player = player;
        ConversationFactory factory = new ConversationFactory(ProAnnouncer.getInstance());
        factory.withFirstPrompt(new PromptReply());
        factory.buildConversation(player).begin();
    }

    public abstract void reply(String s);

    private class PromptReply extends ValidatingPrompt {

        @Override
        protected boolean isInputValid(ConversationContext conversationContext, String s) {
            return true;
        }

        @Override
        protected Prompt acceptValidatedInput(ConversationContext conversationContext, String s) {
            reply(s);
            return Prompt.END_OF_CONVERSATION;
        }

        @Override
        public String getPromptText(ConversationContext conversationContext) {
            return prompt;
        }
    }

}

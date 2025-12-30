package org.saadeddine.telegramchatbot.agents;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;

import java.util.Arrays;

@Component
public class AIAgent {
    private ChatClient chatClient;

    public AIAgent(ChatClient.Builder builder,
                   ChatMemory memory, ToolCallbackProvider tools) {
        Arrays.stream(tools.getToolCallbacks()).forEach(toolCallback -> {
            System.out.println("-------------------------------------");
            System.out.println(toolCallback.getToolDefinition());
            System.out.println("-------------------------------------");
        });
        this.chatClient = builder.defaultSystem(
                        """
                                Vous un assistant qui se charge de repondre aux question de l'utilisateur en fonction du contexte fourni.
                                 si aucun contexte n'est fourni, repond avec je ne sais pas"""
                )
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(memory).build()
                )


                .build();
    }

    @GetMapping("/chat")
    public String askAgent(@RequestParam String query) {
        return chatClient.prompt()
                .user(query)
                .call().content();
    }
}

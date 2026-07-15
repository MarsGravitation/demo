package com.microwu.cxd;

import com.microwu.cxd.service.ChatAssistant;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.junit.jupiter.api.Test;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2026/2/2   15:34
 * Update History:
 * Author        Time            Content
 */
public class RagTest {

    @Test
    public void test() {
        OpenAiChatModel model = OpenAiChatModel.builder()
                .apiKey(System.getenv("aliQwen-api"))
                .modelName("qwen3-max")
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .build();

        InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

        ChatAssistant assistant = AiServices.builder(ChatAssistant.class)
                .chatModel(model)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(50))
                .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
                .build();

        Document document = FileSystemDocumentLoader.loadDocument("D:\\WorkSpace\\demo\\langchain\\langchain\\chat-rag\\src\\main\\resources\\a.txt");
        EmbeddingStoreIngestor.ingest(document, embeddingStore);

        String result = assistant.chat("1000对应的含义是什么");
        System.out.println(result);
    }

}

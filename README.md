# 🎬 YouTube AI SEO Toolkit

A Spring Boot web application that helps YouTube creators optimize their content with AI-powered SEO tag suggestions, thumbnail downloads, and video data retrieval.

---

## ✨ Features

- **🔍 SEO Tag Generator** — Search by video title, extract tags from the top matching video and related videos
- **🤖 AI Tag Suggestions** — Groq LLaMA 3.3 70B generates 10 additional high-relevance SEO tags based on your title and existing tags
- **🖼️ Thumbnail Downloader** — Paste any YouTube URL or video ID to fetch and download the HQ thumbnail
- **📄 Video Data Retriever** — Fetch complete video metadata: title, channel, description, publish date, tags, and thumbnail

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 21, Spring Boot 4.0.5 |
| Web | Spring WebMVC, Spring WebFlux (WebClient) |
| Templating | Thymeleaf |
| AI | Groq API (LLaMA 3.3 70B) |
| YouTube Data | YouTube Data API v3 |
| Build | Maven |
| Utilities | Lombok, Jackson |

---

## 🚀 Getting Started

### Prerequisites

- Java 21+
- Maven 3.8+
- [YouTube Data API v3 key](https://console.cloud.google.com/)
- [Groq API key](https://console.groq.com/)

### Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/shailendra849/YouTube-AI-SEO-Toolkit.git
   cd YouTube-AI-SEO-Toolkit/YouTubeTools
   ```

2. **Configure API keys**

   Create `src/main/resources/application-local.properties`:
   ```properties
   youtube.api.key=YOUR_YOUTUBE_API_KEY
   openai.api.key=YOUR_GROQ_API_KEY
   ai.provider=groq
   ```

   The main `application.properties` already has:
   ```properties
   spring.profiles.active=local
   ```

3. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Open in browser**
   ```
   http://localhost:9090
   ```

---

## 📁 Project Structure

```
src/main/java/com/YouTubeTools/
├── Controller/
│   ├── YouTubeTagsController.java   # SEO tag search + AI suggestions
│   ├── YouTubeVideoController.java  # Video metadata retrieval
│   ├── ThumbnailController.java     # Thumbnail downloader
│   └── PageController.java
├── Service/
│   ├── YouTubeService.java          # YouTube Data API integration
│   ├── AIService.java               # Groq/OpenAI API integration
│   └── ThumbnailService.java        # YouTube URL/ID parsing
└── Model/
    ├── Video.java
    └── SearchVideo.java

src/main/resources/
├── templates/
│   ├── home.html                    # SEO Tag Generator page
│   ├── video-details.html           # Video Data Retriever page
│   ├── thumbnails.html              # Thumbnail Downloader page
│   └── fragments/navbar.html
└── application.properties
```

---

## 🔑 Environment Variables

| Property | Description |
|---|---|
| `youtube.api.key` | YouTube Data API v3 key |
| `openai.api.key` | Groq API key |
| `ai.provider` | AI provider — `groq` or `openai` |

> ⚠️ Never commit real API keys. Use `application-local.properties` (gitignored) for local development.

---

## 🤖 AI Provider Configuration

The app supports both **Groq** (default, free) and **OpenAI**:

```properties
# Use Groq (recommended - free tier available)
ai.provider=groq
openai.api.key=YOUR_GROQ_KEY

# Use OpenAI
ai.provider=openai
openai.api.key=YOUR_OPENAI_KEY
```

---

## 📸 Pages

| Route | Description |
|---|---|
| `/` | SEO Tag Generator (home) |
| `/thumbnail` | YouTube Thumbnail Downloader |
| `/youtube/video-details` | Video Data Retriever |

---

## 👨‍💻 Author

**Shailendra Dwivedi**  
B.Tech ECE — UIT RGPV Bhopal  
[GitHub](https://github.com/shailendra849) · [LinkedIn](https://linkedin.com/in/shailendra-dwivedi)

---

## 📄 License

This project is open-source and available under the [MIT License](LICENSE).

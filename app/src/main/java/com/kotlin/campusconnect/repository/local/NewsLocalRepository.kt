package com.kotlin.campusconnect.repository.local

import com.kotlin.campusconnect.db.ArticleDatabase
import com.kotlin.campusconnect.models.Article

class NewsLocalRepository(private val db: ArticleDatabase) {

    suspend fun insert(article: Article) = db.getArticleDao().insert(article)

    fun getFavoriteNews() = db.getArticleDao().getAllArticles()

    fun getFavoriteNewsByPublishedAt(publishedAt: String) =
        db.getArticleDao().getArticleByPublishedAt(publishedAt)

    suspend fun deleteArticle(article: Article): Int = db.getArticleDao().deleteArticle(article)

    suspend fun deleteArticleByPublishedAt(publishedAt: String): Int =
        db.getArticleDao().deleteArticleByPublishedAt(publishedAt)
}
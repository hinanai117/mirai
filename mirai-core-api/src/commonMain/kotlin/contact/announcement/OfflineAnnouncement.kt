/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/mamoe/mirai/blob/dev/LICENSE
 */

@file:Suppress("NOTHING_TO_INLINE")

package net.mamoe.mirai.contact.announcement

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.mamoe.mirai.utils.cast
import net.mamoe.mirai.utils.copy
import net.mamoe.mirai.utils.map
import net.mamoe.mirai.utils.safeCast

/**
 * 表示在本地构建的, 无唯一标识符的 [Announcement].
 * @see OnlineAnnouncement.publishTo
 *
 * @since 2.7
 */
@Serializable(OfflineAnnouncement.Companion.Serializer::class)
@SerialName(OfflineAnnouncement.SERIAL_NAME)
public sealed interface OfflineAnnouncement : Announcement {
    public companion object {
        public const val SERIAL_NAME: String = "OfflineAnnouncement"

        /**
         * 创建 [OfflineAnnouncement]. 若 [announcement] 类型为 [OfflineAnnouncement] 则直接返回 [announcement].
         */
        @JvmStatic
        public inline fun from(announcement: Announcement): OfflineAnnouncement =
            announcement.safeCast() ?: announcement.run { create(content, parameters) }

        /**
         * 创建 [OfflineAnnouncement].
         * @param content 公告内容
         * @param parameters 附加参数
         */
        @JvmStatic
        public fun create(
            content: String,
            parameters: AnnouncementParameters = AnnouncementParameters.DEFAULT
        ): OfflineAnnouncement = OfflineAnnouncementImpl(content, parameters)

        /**
         * 创建 [AnnouncementParameters] 并创建 [OfflineAnnouncement].
         * @param content 公告内容
         * @param parameters 附加参数
         * @see AnnouncementParametersBuilder
         */
        @JvmStatic
        public inline fun create(
            content: String,
            parameters: AnnouncementParametersBuilder.() -> Unit
        ): OfflineAnnouncement = create(content, buildAnnouncementParameters(parameters))

        internal object Serializer : KSerializer<OfflineAnnouncement> by OfflineAnnouncementImpl.serializer().map(
            resultantDescriptor = OfflineAnnouncementImpl.serializer().descriptor.copy(SERIAL_NAME),
            deserialize = { it },
            serialize = { it.safeCast<OfflineAnnouncementImpl>() ?: create(content, parameters).cast() }
        )
    }
}

@SerialName(OfflineAnnouncement.SERIAL_NAME)
@Serializable
private data class OfflineAnnouncementImpl(
    override val content: String,
    override val parameters: AnnouncementParameters
) : OfflineAnnouncement {
    override fun toString() = "OfflineAnnouncement(body='$content', parameters=$parameters)"
}
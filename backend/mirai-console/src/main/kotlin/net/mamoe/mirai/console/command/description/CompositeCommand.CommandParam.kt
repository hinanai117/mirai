@file:Suppress("unused")

package net.mamoe.mirai.console.command.description

import net.mamoe.mirai.console.command.Command
import net.mamoe.mirai.console.command.CompositeCommand
import java.lang.reflect.Parameter
import kotlin.reflect.KClass

internal fun Parameter.toCommandParam(): CommandParam<*> {
    val name = getAnnotation(CompositeCommand.Name::class.java)
    return CommandParam(
        name?.name ?: this.name ?: throw IllegalArgumentException("Cannot construct CommandParam from a unnamed param"),
        this.type.kotlin
    )
}

/**
 * 指令形式参数.
 * @see toCommandParam
 */
internal data class CommandParam<T : Any>(
    /**
     * 参数名. 不允许重复.
     */
    val name: String,
    /**
     * 参数类型. 将从 [CommandDescriptor.context] 中寻找 [CommandArgParser] 解析.
     */
    val type: KClass<T> // exact type
) {
    constructor(name: String, type: KClass<T>, parser: CommandArgParser<T>) : this(name, type) {
        this._overrideParser = parser
    }

    @Suppress("PropertyName")
    @JvmField
    internal var _overrideParser: CommandArgParser<T>? = null


    /**
     * 覆盖的 [CommandArgParser].
     *
     * 如果非 `null`, 将不会从 [CommandParserContext] 寻找 [CommandArgParser]
     *
     * @see Command.parserFor
     */
    val overrideParser: CommandArgParser<T>? get() = _overrideParser
}


package com.chao.failfast;

import com.chao.failfast.internal.Chain;

/**
 * Fail-Fast 静态入口类 - 链式验证API
 * 提供流畅的链式调用接口，支持快速失败和严格模式两种验证策略
 * 通过静态方法提供各种常见验证场景的便捷入口
 */
public final class Failure {

    private Failure() {
    }

    /**
     * 开始一个新的验证链（默认快速失败模式）
     *
     * @return 新的 Chain 实例
     */
    public static Chain begin() {
        return Chain.begin(true);
    }

    /**
     * 开始一个新的验证链（全量收集模式）
     * 在此模式下，验证失败不会立即抛出异常，而是收集所有错误
     *
     * @return 新的 Chain 实例
     */
    public static Chain strict() {
        return Chain.begin(false);
    }
}

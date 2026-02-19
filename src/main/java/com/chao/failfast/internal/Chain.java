package com.chao.failfast.internal;

import com.chao.failfast.internal.chain.TerminatingChain;

/**
 * 验证链 - 支持FailFast和非FailFast双模式
 * 提供流畅的链式验证API，支持快速失败和全量收集两种验证策略
 */
public final class Chain extends TerminatingChain<Chain> {

    private Chain(boolean failFast) {
        super(failFast);
    }

    public static Chain begin(boolean failFast) {
        return new Chain(failFast);
    }
}

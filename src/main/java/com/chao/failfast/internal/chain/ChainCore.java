package com.chao.failfast.internal.chain;

import com.chao.failfast.annotation.FastValidator.ValidationContext;
import com.chao.failfast.internal.Business;
import com.chao.failfast.internal.core.ResponseCode;
import com.chao.failfast.internal.core.ViolationSpec;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 验证链核心 - 管理状态（failFast, alive, errors, context）
 */
public abstract class ChainCore<S extends ChainCore<S>> {
    @Getter
    protected final boolean failFast;
    @Getter
    protected boolean alive = true;
    protected final ValidationContext context;
    protected final List<Business> errors = new ArrayList<>();

    protected ChainCore(boolean failFast, ValidationContext context) {
        this.failFast = failFast;
        this.context = context;
    }

    protected boolean shouldSkip() {
        if (context != null && context.isStopped()) return true;
        return (!alive && failFast);
    }

    @SuppressWarnings("unchecked")
    protected S self() {
        return (S) this;
    }

    /**
     * 统一校验入口 - 支持配置
     */
    protected S check(boolean condition, Consumer<ViolationSpec> configurer) {
        if (shouldSkip()) return self();
        if (!condition) {
            ViolationSpec spec = new ViolationSpec();
            configurer.accept(spec);
            addError(spec);
            if (failFast) alive = false;
        }
        return self();
    }

    /**
     * 无配置校验 - 使用默认错误
     */
    protected S check(boolean condition) {
        return check(condition, spec -> {
        });
    }

    private void addError(ViolationSpec spec) {
        Business business = buildBusiness(spec);

        if (context != null) {
            context.reportError(business);
            if (failFast) context.stop();
        } else {
            errors.add(business);
        }
    }

    private Business buildBusiness(ViolationSpec spec) {
        if (spec.hasFabricator()) {
            Business.Fabricator fab = Business.compose();
            spec.getFabricator().accept(fab);
            return fab.materialize();
        }
        if (spec.getCode() != null && spec.getDetail() != null) {
            return Business.of(spec.getCode(), spec.getDetail());
        }
        if (spec.getCode() != null) {
            return Business.of(spec.getCode());
        }
        return Business.of(ResponseCode.of(
                500,
                "Validation failed",
                "链式验证未通过，请使用 ViolationSpec 配置具体错误信息"
        ));
    }

    /**
     * 获取核心实例（供接口默认方法使用）
     */
    protected S core() {
        return self();
    }

    /**
     * 获取业务原因列表
     * 该方法返回一个新的ArrayList，包含所有的错误信息
     *
     * @return 返回一个Business类型的列表，包含所有错误信息
     */
    public List<Business> getCauses() {
        return new ArrayList<>(errors);
    }

    /**
     * 检查当前对象是否有效的公共方法
     *
     * @return 如果错误集合为空且对象处于活跃状态则返回true，否则返回false
     */
    public boolean isValid() {
        return errors.isEmpty() && alive;
    }
}
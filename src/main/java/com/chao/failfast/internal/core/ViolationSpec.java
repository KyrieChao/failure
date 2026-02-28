package com.chao.failfast.internal.core;

import com.chao.failfast.internal.Business;
import lombok.Getter;

import java.util.function.Consumer;

/**
 * 用于链式配置错误信息
 */
public final class ViolationSpec {
    @Getter
    private ResponseCode code;
    @Getter
    private String detail;
    private Consumer<Business.Fabricator> fabricatorConsumer;

    public ViolationSpec responseCode(ResponseCode code) {
        this.code = code;
        return this;
    }

    public ViolationSpec detail(String detail) {
        this.detail = detail;
        return this;
    }

    /**
     * 设置业务伪造器（Fabricator）的消费者接口
     * 该方法用于设置对业务伪造器的操作，返回当前对象以支持链式调用
     *
     * @param consumer 对Business.Fabricator的操作接口，用于定义对伪造器的具体操作
     * @return 返回当前ViolationSpec对象，支持链式调用
     */
    public ViolationSpec fabricator(Consumer<Business.Fabricator> consumer) {
        this.fabricatorConsumer = consumer;
        return this;
    }

    /**
     * 获取用于处理Business.Fabricator类型对象的Consumer接口实现
     * Consumer是一个函数式接口，表示接受一个输入参数且不返回结果的操作
     *
     * @return 返回一个Consumer<Business.Fabricator>类型的对象，用于处理Business.Fabricator实例
     */
    public Consumer<Business.Fabricator> getFabricator() {
        return fabricatorConsumer;
    }

    /**
     * 检查是否存在制造器(fabricator)
     * 该方法用于判断fabricatorConsumer对象是否已经被初始化
     *
     * @return 如果fabricatorConsumer不为null，返回true；否则返回false
     */
    public boolean hasFabricator() {
        return fabricatorConsumer != null;
    }
}
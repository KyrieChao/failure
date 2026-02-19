package com.chao.failfast.internal.chain;

import com.chao.failfast.internal.Business;
import com.chao.failfast.internal.ResponseCode;

import java.util.Date;
import java.util.function.Consumer;

/**
 * 日期校验链
 * 提供日期先后顺序校验功能
 */
public abstract class DateChain<C extends DateChain<C>> extends NumberChain<C> {

    protected DateChain(boolean failFast) {
        super(failFast);
    }

    /**
     * 验证日期是否在指定日期之后
     *
     * @param date1 待验证日期
     * @param date2 比较基准日期
     * @return 当前链实例
     */
    public C after(Date date1, Date date2) {
        return check(date1 != null && date2 != null && date1.after(date2));
    }

    /**
     * 验证日期是否在指定日期之后，失败时使用指定错误码
     *
     * @param date1 待验证日期
     * @param date2 比较基准日期
     * @param code  错误码
     * @return 当前链实例
     */
    public C after(Date date1, Date date2, ResponseCode code) {
        return check(date1 != null && date2 != null && date1.after(date2), code);
    }

    /**
     * 验证日期是否在指定日期之后，失败时使用自定义错误构建器
     *
     * @param date1    待验证日期
     * @param date2    比较基准日期
     * @param consumer 错误构建器消费者
     * @return 当前链实例
     */
    public C after(Date date1, Date date2, Consumer<Business.Fabricator> consumer) {
        return check(date1 != null && date2 != null && date1.after(date2), consumer);
    }

    /**
     * 验证日期是否在指定日期之前
     *
     * @param date1 待验证日期
     * @param date2 比较基准日期
     * @return 当前链实例
     */
    public C before(Date date1, Date date2) {
        return check(date1 != null && date2 != null && date1.before(date2));
    }

    /**
     * 验证日期是否在指定日期之前，失败时使用指定错误码
     *
     * @param date1 待验证日期
     * @param date2 比较基准日期
     * @param code  错误码
     * @return 当前链实例
     */
    public C before(Date date1, Date date2, ResponseCode code) {
        return check(date1 != null && date2 != null && date1.before(date2), code);
    }

    /**
     * 验证日期是否在指定日期之前，失败时使用自定义错误构建器
     *
     * @param date1    待验证日期
     * @param date2    比较基准日期
     * @param consumer 错误构建器消费者
     * @return 当前链实例
     */
    public C before(Date date1, Date date2, Consumer<Business.Fabricator> consumer) {
        return check(date1 != null && date2 != null && date1.before(date2), consumer);
    }
}

package com.zhuzhou.page;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhuzhou.framework.form.Form;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author:   chenzeting
 * Date:     2018/12/27
 * Description: 分页查询统一实体
 */
@Data
@Slf4j
@NoArgsConstructor
public class PageForm<T> extends Page<T> implements Form {

    public PageForm(long current, long size, boolean isSearchCount) {
        super(current, size,isSearchCount);
    }

    /**
     * 是否进行 count 查询
     * @param flag
     */
    public PageForm<T> setSearchCount (Boolean flag) {
        return new PageForm(this.getCurrent(), this.getSize(), flag);
    }
}

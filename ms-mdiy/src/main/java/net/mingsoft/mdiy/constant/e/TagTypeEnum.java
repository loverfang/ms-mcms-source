/**
 * The MIT License (MIT) * Copyright (c) 2020 铭软科技(mingsoft.net)

 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package net.mingsoft.mdiy.constant.e;

/**
 * @Author: 铭软开发
 * @Description:
 * @Date: Create in 2020/06/23 14:18
 */
public enum  TagTypeEnum {
    /**
     * 单标签
     */
    SINGLE("single"),
    /**
     * 分页标签
     */
    PAGE("page"),
    /**
     * 列表标签
     */
    LIST("list");

    TagTypeEnum(String type) {
        this.type = type;
    }

    private String type;

    public static TagTypeEnum get(String type) {
        for (TagTypeEnum e : TagTypeEnum.values()) {
            if (e.type.equals(type)) {
                return e;
            }
        }
        return null;
    }
}

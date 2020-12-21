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
package net.mingsoft.basic.constant;

import net.mingsoft.base.constant.e.BaseEnum;

/**
 * 
 * 错误编码枚举类
 * @author 铭飞开源团队-wjj  
 * @date 2018年9月15日
 */
public enum ErrorCodeEnum implements BaseEnum {
	/**
	 * 200  正常；请求已完成
	 */
	SUCCESS(200),
	/**
	 * 201  正常；紧接 POST 命令
	 */
	SUCCESS_POST(201),
	
	/**
	 * 202  正常；已接受用于处理，但处理尚未完成。
	 */
	SUCCESS_LOADING(202),
	
	/**
	 * 203  正常；部分信息 — 返回的信息只是一部分。
	 */
	SUCCESS_PART(203),
	/**
	 * 204  正常；无响应 — 已接收请求，但不存在要回送的信息。
	 */
	SUCCESS_NO_RESPONSE(204),
	
	/**
	 * 301  已移动 — 请求的数据具有新的位置且更改是永久的。
	 */
	REDIRECT_REMOVE(301),
	/**
	 * 302  已找到 — 请求的数据临时具有不同 URI。  
	 */
	REDIRECT_FIND(302),
	/**
	 * 303  请参阅其它 — 可在另一 URI 下找到对请求的响应，且应使用 GET 方法检索此响应。  
	 */
	REDIRECT_OTHER(303),
	/**
	 * 304  未修改 — 未按预期修改文档。  
	 */
	REDIRECT_NOT_CHANGED(304),
	/**
	 * 400  错误请求 — 请求中有语法问题，或不能满足请求
	 */
	CLIENT_REQUEST(400),
	/**
	 * 401  未授权 — 未授权客户机访问数据。  
	 */
	CLIENT_UNAUTHORIZED(401),
	/**
	 * 402  需要付款 — 表示计费系统已有效。
	 */
	CLIENT_NEED_PAY(402),
	/**
	 * 403  禁止 — 即使有授权也不需要访问。
	 */
	CLIENT_NOT_FIND(403),
	
	/**
	 *  500  内部错误 — 因为意外情况，服务器不能完成请求。
	 */
	SERVER_ERROR(500),
	/**
	 * 501  未执行 — 服务器不支持请求的工具。  
	 */
	SERVER_NOT_SUPPORT(501),
	/**
	 * 502  错误网关 — 服务器接收到来自上游服务器的无效响应。 
	 */
	SERVER_ERROR_GATEWAY(502),
	/**
	 * 503  无法获得服务 — 由于临时过载或维护，服务器无法处理请求。
	 */
	SERVER_NOT_FIND(503);
	
	ErrorCodeEnum(Object code) {
		this.code = code;
	}

	private Object code;

	@Override
	public String toString() {
		return code.toString();
	}

	public int toInt() {
		return Integer.parseInt(code.toString());
	}
}

package com.willy.test.FastELSample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.greenpineyu.fel.Expression;
import com.greenpineyu.fel.FelEngine;
import com.greenpineyu.fel.FelEngineImpl;
import com.greenpineyu.fel.common.ObjectUtils;
import com.greenpineyu.fel.context.AbstractContext;
import com.greenpineyu.fel.context.ContextChain;
import com.greenpineyu.fel.context.FelContext;
import com.greenpineyu.fel.context.MapContext;
import com.greenpineyu.fel.function.CommonFunction;
import com.greenpineyu.fel.function.Function;
import com.greenpineyu.fel.interpreter.ConstInterpreter;
import com.greenpineyu.fel.interpreter.Interpreter;
import com.greenpineyu.fel.optimizer.Interpreters;
import com.greenpineyu.fel.parser.FelNode;
import com.willy.test.FastELSample.func.Foo;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		booleanFunction();
		System.out.println("--------------------");
		simpleFunction();
		System.out.println("--------------------");
		funcWithVariance();
		System.out.println("--------------------");
		funcWithObjectParam();
		System.out.println("--------------------");
		visitCollection();
		System.out.println("--------------------");
		callMethod();
		System.out.println("--------------------");
		context();
		
		
		
		
		
		System.out.println("--------------------");
		contexts();
		System.out.println("--------------------");
		userFunction();
		System.out.println("--------------------");
		userInterpreter();
		System.out.println("--------------------");
		massData();
		System.out.println("--------------------");
		operatorOverload();
		System.out.println("--------------------");
		testCompile();
//		System.out.println("--------------------");
//		testBigNumber();
		// FelContext ctx = fel.getContext();
		// ctx.set("單價", "5000");
		// ctx.set("數量", new Integer(12));
		// ctx.set("運費", "7500");
		// Object result = fel.eval("單價*數量+運費");
		// System.out.println(result);
	}

	/**
	 * 入門
	 */
	public static void simpleFunction() {
		Object result = getEngine().eval("5000*12+7500");
		System.out.println(result);
	}
	
	public static void booleanFunction() {
		FelEngine engine = getEngine();
		Object result = engine.eval("5>2");
		System.out.println(result);
		result = engine.eval("5<2");
		System.out.println(result);
	}

	/**
	 * 使用變數
	 */
	public static void funcWithVariance() {
		FelEngine fel = getEngine();
		FelContext ctx = fel.getContext();
		ctx.set("單價", 5000);
		ctx.set("數量", 12);
		ctx.set("運費", 7500);
		Object result = fel.eval("單價*數量+運費");
		System.out.println(result);
	}

	/**
	 * 獲取物件屬性
	 */
	public static void funcWithObjectParam() {
		FelEngine fel = getEngine();
		FelContext ctx = fel.getContext();
		Foo foo = new Foo();
		ctx.set("foo", foo);
		Map<String, String> m = new HashMap<String, String>();
		m.put("ElName", "fel");
		ctx.set("m", m);

		// 調用foo.getSize()方法。
		Object result = fel.eval("foo.size");

		// 調用foo.isSample()方法。
		result = fel.eval("foo.sample");

		// foo沒有name、getName、isName方法
		// foo.name會調用foo.get("name")方法。
		result = fel.eval("foo.name");

		// m.ElName會調用m.get("ElName");
		result = fel.eval("m.ElName");
	}

	/**
	 * 調用物件的方法
	 */
	public static void callMethod() {
		FelEngine fel = getEngine();
		FelContext ctx = fel.getContext();
		ctx.set("out", System.out);
		fel.eval("out.println('Hello Everybody'.substring(6))");
	}

	/**
	 * 訪問陣列、集合
	 */
	public static void visitCollection() {
		FelEngine fel = getEngine();
		FelContext ctx = fel.getContext();

		// 陣列
		int[] intArray = { 1, 2, 3 };
		ctx.set("intArray", intArray);
		// 獲取intArray[0]
		String exp = "intArray[0]";
		System.out.println(exp + "->" + fel.eval(exp));

		// List
		List<Integer> list = Arrays.asList(1, 2, 3);
		ctx.set("list", list);
		// 獲取list.get(0)
		exp = "list[0]";
		System.out.println(exp + "->" + fel.eval(exp));

		// 集合
		Collection<String> coll = Arrays.asList("a", "b", "c");
		ctx.set("coll", coll);
		// 獲取集合最前面的元素。執行結果為"a"
		exp = "coll[0]";
		System.out.println(exp + "->" + fel.eval(exp));

		// 反覆運算器
		Iterator<String> iterator = coll.iterator();
		ctx.set("iterator", iterator);
		// 獲取反覆運算器最前面的元素。執行結果為"a"
		exp = "iterator[0]";
		System.out.println(exp + "->" + fel.eval(exp));

		// Map
		Map<String, String> m = new HashMap<String, String>();
		m.put("name", "HashMap");
		ctx.set("map", m);
		exp = "map.name";
		System.out.println(exp + "->" + fel.eval(exp));

		// 多維陣列
		int[][] intArrays = { { 11, 12 }, { 21, 22 } };
		ctx.set("intArrays", intArrays);
		exp = "intArrays[0][0]";
		System.out.println(exp + "->" + fel.eval(exp));

		// 多維綜合體，支援陣列、集合的任意組合。
		List<int[]> listArray = new ArrayList<int[]>();
		listArray.add(new int[] { 1, 2, 3 });
		listArray.add(new int[] { 4, 5, 6 });
		ctx.set("listArray", listArray);
		exp = "listArray[0][0]";
		System.out.println(exp + "->" + fel.eval(exp));
	}

	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 自訂上下文環境
	 */
	public static void context() {
		// 負責提供氣象服務的上下文環境
		FelContext ctx = new AbstractContext() {
//			@Override
			public Object get(String name) {
				if ("天氣".equals(name)) {
					return "晴";
				}
				if ("溫度".equals(name)) {
					return 25;
				}
				return null;
			}

		};
		FelEngine fel = new FelEngineImpl(ctx);
		String exp = "'天氣:'+天氣+';溫度:'+溫度";
		Object eval = fel.compile(exp, ctx).eval(ctx);
		System.out.println(eval);
	}

	/**
	 * 多層次上下文環境(變數命名空間)
	 */
	public static void contexts() {
		FelEngine fel = getEngine();
		String costStr = "成本";
		String priceStr = "價格";
		FelContext baseCtx = fel.getContext();
		// 父級上下文中設置成本和價格
		baseCtx.set(costStr, 50);
		baseCtx.set(priceStr, 100);

		String exp = priceStr + "-" + costStr;
		Object baseCost = fel.eval(exp);
		System.out.println("期望利潤：" + baseCost);

		FelContext ctx = new ContextChain(baseCtx, new MapContext());
		// 通貨膨脹導致成本增加（子級上下文 中設置成本，會覆蓋父級上下文中的成本）
		ctx.set(costStr, 50 + 20);
		Object allCost = fel.eval(exp, ctx);
		System.out.println("實際利潤：" + allCost);
	}

	private static FelEngine getEngine() {
		return new FelEngineImpl();
	}

	//0.9版才支持
//	public static void testBigNumber() {
//		// 構建大數值計算引擎
//		FelEngine fel = FelBuilder.bigNumberEngine();
//		String input = "111111111111111111111111111111+22222222222222222222222222222222";
//		Object value = fel.eval(input);
//		Object compileValue = fel.compile(input, fel.getContext()).eval(fel.getContext());
//		System.out.println("大數值計算（解釋執行）:" + value);
//		System.out.println("大數值計算（編譯執行）:" + compileValue);
//	}

	public static void testCompile() {
		FelEngine fel = getEngine();
		FelContext ctx = fel.getContext();
		ctx.set("單價", 5000);
		ctx.set("數量", 12);
		ctx.set("運費", 7500);
		Expression exp = fel.compile("單價*數量+運費", ctx);
		Object result = exp.eval(ctx);
		System.out.println(result);
	}

	public static void userFunction() {
		// 定義hello函數
		Function fun = new CommonFunction() {

			public String getName() {
				return "hello";
			}

			/*
			 * 調用hello("xxx")時執行的代碼
			 */
			@Override
			public Object call(Object[] arguments) {
				Object msg = null;
				if (arguments != null && arguments.length > 0) {
					msg = arguments[0];
				}
				return ObjectUtils.toString(msg);
			}

		};
		FelEngine e = getEngine();
		// 添加函數到引擎中。
		e.addFun(fun);
		String exp = "hello('fel')";
		// 解釋執行
		Object eval = e.eval(exp);
		System.out.println("hello " + eval);
		// 編譯執行
		Expression compile = e.compile(exp, null);
		eval = compile.eval(null);
		System.out.println("hello " + eval);
	}

	/**
	 * 
	 */
	public static void testCompileX() {
		FelEngine fel = getEngine();
		String exp = "單價*數量";
		final MutableInt index = new MutableInt(0);

		// 資料庫中單價列的記錄
		final int[] price = new int[] { 2, 3, 4 };
		// 資料庫中數量列的記錄
		final double[] number = new double[] { 10.99, 20.99, 9.9 };
		FelContext context = new AbstractContext() {

			public Object get(String name) {
				if ("單價".equals(name)) {
					return price[index.intValue()];
				}
				if ("數量".equals(name)) {
					return number[index.intValue()];
				}
				return null;
			}
		};
		Expression compExp = fel.compile(exp, context);
		for (int i = 0; i < number.length; i++) {
			index.setValue(i);
			Object eval = compExp.eval(context);
			System.out.println("總價[" + price[i] + "*" + number[i] + "=" + eval + "]");
		}
	}

	/**
	 * 自訂 解譯器
	 */
	public static void userInterpreter() {
		FelEngine fel = getEngine();
		String costStr = "成本";
		FelContext rootContext = fel.getContext();
		rootContext.set(costStr, "60000");
		FelNode node = fel.parse(costStr);
		// 將變數解析成常量
		node.setInterpreter(new ConstInterpreter(rootContext, node));
		System.out.println(node.eval(rootContext));
	}

	/**
	 * 大資料量計算（計算1千萬次)
	 */
	public static void massData() {
		FelEngine fel = getEngine();
		final Interpreters opti = new Interpreters();
		final MutableInt index = new MutableInt(0);
		int count = 10 * 1000 * 1000;
		final double[] counts = new double[count];
		final double[] prices = new double[count];
		Arrays.fill(counts, 10d);
		Arrays.fill(prices, 2.5d);
		opti.add("單價", new Interpreter() {
			public Object interpret(FelContext context, FelNode node) {
				return prices[index.intValue()];
			}
		});
		opti.add("數量", new Interpreter() {
			public Object interpret(FelContext context, FelNode node) {
				return counts[index.intValue()];
			}
		});
		Expression expObj = fel.compile("單價*數量", null, opti);
		long start = System.currentTimeMillis();
		Object result = null;
		for (int i = 0; i < count; i++) {
			result = expObj.eval(null);
			index.increment();
		}
		long end = System.currentTimeMillis();

		System.out.println("大資料量計算:" + result + ";耗時:" + (end - start));
	}

	/**
	 * 操作符重載，使用自訂解譯器實現操作符重載
	 */
	public static void operatorOverload() {
		/*
		 * 擴展Fel的+運算子，使其支援陣列+陣列
		 */

		FelEngine fel = getEngine();
		// 單價
		double[] price = new double[] { 2, 3, 4 };
		// 費用
		double[] cost = new double[] { 0.3, 0.3, 0.4 };
		FelContext ctx = fel.getContext();
		ctx.set("單價", price);
		ctx.set("費用", cost);
		String exp = "單價+費用";
		Interpreters interpreters = new Interpreters();
		// 定義"+"操作符的解釋方法。
		interpreters.add("+", new Interpreter() {
//			@Override
			public Object interpret(FelContext context, FelNode node) {
				List<FelNode> args = node.getChildren();
				double[] leftArg = (double[]) args.get(0).eval(context);
				double[] rightArg = (double[]) args.get(1).eval(context);
				return sum(leftArg) + sum(rightArg);
			}

			// 對陣列進行求和
			public double sum(double[] array) {
				double d = 0;
				for (int i = 0; i < array.length; i++) {
					d += array[i];
				}
				return d;
			}
		});

		// 使用自訂解譯器作為編譯選項進行進行編譯
		Expression expObj = fel.compile(exp, null, interpreters);
		Object eval = expObj.eval(ctx);
		System.out.println("陣列相加:" + eval);
	}

	public static void testSpeed() {
		FelEngine fel = getEngine();
		String exp = "40.52334+60*(21.8144+17*32.663)";
		FelNode node = fel.parse(exp);
		int times = 100 * 1000 * 1000;
		long s1 = System.currentTimeMillis();
		for (int i = 0; i < times; i++) {
			// double j = 40.52334 + 60 * (21.8144 + 17 * 32.663);
			node.eval(null);
		}
		long s2 = System.currentTimeMillis();
		System.out.println("花費的時間:" + (s2 - s1));
	}

}

class ColumnInterpreter implements Interpreter {
	MutableInt index;

	double[] records;

	ColumnInterpreter(MutableInt index, double[] records) {
		this.index = index;
		this.records = records;
	}

//	@Override
	public Object interpret(FelContext context, FelNode node) {
		return records[index.intValue()];
	}
}

class MutableInt {
	private int value;

	public MutableInt(int i) {
		this.value = i;
	}

	public int intValue() {
		return value;
	}

	public void setValue(int i) {
		this.value = i;
	}

	public void increment() {
		value++;
	}

}

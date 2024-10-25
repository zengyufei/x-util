package full;

import full.test.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class FullTest {

    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        testClass(AnyMatchTest.class);
        testClass(CloneTest.class);
        testClass(ConcatTest.class);
        testClass(CountTest.class);
        testClass(DiffTest.class);
        testClass(DistinctTest.class);
        testClass(FilterNotNullTest.class);
        testClass(FilterNullTest.class);
        testClass(FilterOrsTest.class);
        testClass(FiltersTest.class);
        testClass(FindFirstTest.class);
        testClass(FlatMapTest.class);
        testClass(ForEachTest.class);
        testClass(GroupByTest.class);
        testClass(JoiningTest.class);
        testClass(LimitTest.class);
        testClass(MapTest.class);
        testClass(newArrayListTest.class);
        testClass(newHashMapTest.class);
        testClass(NoneMatchTest.class);
        testClass(OpTest.class);
        testClass(PeekTest.class);
        testClass(ReduceTest.class);
        testClass(ReversedTest.class);
        testClass(SkipTest.class);
        testClass(SortTest.class);
        testClass(SplitTest.class);
        testClass(SubTest.class);
        testClass(SumTest.class);
        testClass(toMapTest.class);
        testClass(TryTest.class);
    }

    private static <T> void testClass(Class<T> clazz) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        final Constructor<T> constructor = clazz.getDeclaredConstructor();
        final T filtersTest = constructor.newInstance();
        for (Method declaredMethod : clazz.getDeclaredMethods()) {
            // 判断该方法是否是静态的
            boolean isStatic = Modifier.isStatic(declaredMethod.getModifiers());
            if (isStatic) {
                continue;
            }
            declaredMethod.invoke(filtersTest);
        }
    }

}

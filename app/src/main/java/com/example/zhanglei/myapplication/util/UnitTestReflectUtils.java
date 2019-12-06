package com.example.zhanglei.myapplication.util;

import android.util.Log;

import org.mockito.Mockito;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UnitTestReflectUtils {

    private static final String TAG = "UnitTestReflectUtils";

    public static void invokeClassAllMethod(Class testClazz) {
        Log.d(TAG, "[UnitTestReflectUtils]-->invokeClassAllMethod: testClassName ==" + testClazz.getSimpleName());
        Class[] declaredClasses = testClazz.getDeclaredClasses();
        List<Class> allTestClass = new ArrayList<>(5);
        allTestClass.add(testClazz);
        allTestClass.addAll(Arrays.asList(declaredClasses));

        for (Class clazz : allTestClass) {
            Log.d(TAG, "[UnitTestReflectUtils]-->invokeClassAllMethod: Start Test ------>className ==" + clazz.getSimpleName());

            Object object = null;
            try {
                object = createObject(clazz, false);
            } catch (Exception e) {
                Log.d(TAG, "[UnitTestReflectUtils]-->invokeClassAllMethod: ", e);
            }

            if (object != null) {
                try {
                    setClassAllFiled(object);
                } catch (Exception e) {
                    Log.d(TAG, "[UnitTestReflectUtils]-->invokeClassAllMethod: ", e);
                }

                Method[] declaredMethods = clazz.getDeclaredMethods();
                for (Method declaredMethod : declaredMethods) {
                    declaredMethod.setAccessible(true);
                    Class<?>[] parameterTypes = declaredMethod.getParameterTypes();
                    try {
                        invokeMethod(object, declaredMethod, parameterTypes);
                    } catch (Exception e) {
                        Log.d(TAG, "[UnitTestReflectUtils]-->invokeClassAllMethod: ", e);
                    }
                }
            }
            Log.d(TAG, "[UnitTestReflectUtils]-->invokeClassAllMethod: End Test ------>className ==" + clazz.getSimpleName());

            Log.d(TAG, "****************************************************\n\n\n");
        }
    }

    private static void setClassAllFiled(Object object) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Log.d(TAG, "[UnitTestReflectUtils]-->setClassAllFiled: ");
        Field[] declaredFields = object.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            Log.d(TAG, "[UnitTestReflectUtils]-->setClassAllFiled: fieldName==" + declaredField.getName());
            if (declaredField.get(object) == null) {
                Log.d(TAG, "[UnitTestReflectUtils]-->setClassAllFiled: field is null, create field object");
                Object field = createObject(declaredField.getType(), false);
                declaredField.set(object, field);
            }
        }
    }

    private static <T> T createObject(Class<T> clazz, boolean isCreateParameterObject) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Log.d(TAG, "[UnitTestReflectUtils]-->createObject: ClassName==" + clazz.getSimpleName());
        int modifiers = clazz.getModifiers();
        T createObject;

        if (Modifier.isFinal(modifiers)) {
            Log.d(TAG, "[UnitTestReflectUtils]-->createObject: is Final ");
        }
        if (Modifier.isAbstract(modifiers)) {
            Log.d(TAG, "[UnitTestReflectUtils]-->createObject: is Abstract ");
        }
        if (Modifier.isInterface(modifiers)) {
            Log.d(TAG, "[UnitTestReflectUtils]-->createObject: is Interface ");
        }
        if (Modifier.isStatic(modifiers)) {
            Log.d(TAG, "[UnitTestReflectUtils]-->createObject: is Static ");
        }

        if (isCreateParameterObject) {
            // if (clazz.isInterface() || Modifier.isAbstract(modifiers)) {
            //     return Mockito.mock(clazz);
            // }

            if ((Modifier.isFinal(modifiers)) && !Modifier.isAbstract(modifiers)) {
                Constructor[] declaredConstructors = clazz.getDeclaredConstructors();
                Constructor constructor = declaredConstructors[0];
                constructor.setAccessible(true);
                Class[] parameterTypes = constructor.getParameterTypes();
                createObject = (T) constructor.newInstance(getAllParameterObjects(parameterTypes, false));
            } else if ((Modifier.isFinal(modifiers)) && Modifier.isAbstract(modifiers)) {
                createObject = null;
            } else {
                createObject = Mockito.mock(clazz);
            }
        } else {
            if (clazz.isInterface() || Modifier.isAbstract(modifiers)) {
                Log.d(TAG, "[UnitTestReflectUtils]-->createObject: isInterface or isAbstract , create object is null");
                return null;
            }
            Constructor[] declaredConstructors = clazz.getDeclaredConstructors();
            Constructor constructor = declaredConstructors[0];
            constructor.setAccessible(true);
            Class[] parameterTypes = constructor.getParameterTypes();
            createObject = (T) constructor.newInstance(getAllParameterObjects(parameterTypes, false));
        }

        return createObject;
    }

    private static void invokeMethod(Object object, Method invokeMethod, Class<?>[] parameterTypes) throws InvocationTargetException, IllegalAccessException, InstantiationException {
        Log.d(TAG, "[UnitTestReflectUtils]-->invokeMethod: ObjectClassName==" + object.getClass().getSimpleName() + ", MethodName==" + invokeMethod.getName());
//        boolean avoidExecution = avoidExecutionBtaAbnormalTestMethod(object, invokeMethod);
//        if (avoidExecution) {
//            return;
//        }
        boolean isSynthetic = invokeMethod.isSynthetic();

        Object[] allParameterObjects = getAllParameterObjects(parameterTypes, isSynthetic);
        invokeMethod.invoke(object, allParameterObjects);
        Log.d(TAG, "[UnitTestReflectUtils]-->invokeMethod: Execute Method ------>" + object.getClass().getSimpleName() + "." + invokeMethod.getName());
    }

    private static boolean avoidExecutionBtaAbnormalTestMethod(Object object, Method invokeMethod) {
        // if (!(object instanceof BtAudioModelFactory) && invokeMethod.getName().equals("onInit")) {
        //     Log.d(TAG, "[UnitTestReflectUtils]-->avoidExecutionBtaAbnormalTestMethod: Object==" + object + ", MethodName==" + invokeMethod.getName());
        //     return true;
        // }
        return false;
    }


    /**
     * @param parameterTypes 一个方法的所有参数的class集合
     * @param isSynthetic    当存在内嵌class定义，而且需要在外包class和内嵌class之间访问对方的private修饰的属性的时候，java编译器就必须创建synthetic method.
     *                       ---->！！！ 请记住:对于java编译器而言，内部类也会被单独编译成一个class文件。那么原有代码中的相关属性可见性就难以维持，synthetic method
     *                       也就是为了这个目的而生成的。生成的synthetic方法是包访问性的static方法
     * @return 一个方法的所有参数对象组成的数组
     */
    private static Object[] getAllParameterObjects(Class<?>[] parameterTypes, boolean isSynthetic) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Log.d(TAG, "[UnitTestReflectUtils]-->getAllParameterObjects: Method is Synthetic == " + isSynthetic);
        if (parameterTypes.length > 0) {
            List<Object> parameterObjectList = new ArrayList<>(5);
            for (Class<?> parameterType : parameterTypes) {
                Log.d(TAG, "[UnitTestReflectUtils]-->getAllParameterObjects: parameterType==" + parameterType.getSimpleName());
                if (parameterType.isPrimitive()) {
                    Object primitiveTypeObject = getPrimitiveTypeObject(parameterType);
                    parameterObjectList.add(primitiveTypeObject);
                } else {
                    if (!isSynthetic) {
                        Object object = createObject(parameterType, true);
                        parameterObjectList.add(object);
                    } else {
                        Object object = createObject(parameterType, false);
                        parameterObjectList.add(object);
                    }
                }
            }
            Object[] objects = parameterObjectList.toArray();
            Log.d(TAG, "[UnitTestReflectUtils]-->getAllParameterObjects: ParameterObjects==" + Arrays.toString(objects));
            return objects;
        } else {
            Log.d(TAG, "[UnitTestReflectUtils]-->getAllParameterObjects: ParameterObjects==null");
            return null;
        }
    }


    private static Object getPrimitiveTypeObject(Class primitiveClass) {
        Log.d(TAG, "[UnitTestReflectUtils]-->getPrimitiveTypeObject: primitiveClassName==" + primitiveClass.getSimpleName());
        Object object = null;
        if (primitiveClass == Byte.TYPE) {
            object = (byte) 0;
        } else if (primitiveClass == Short.TYPE) {
            object = (short) 0;
        } else if (primitiveClass == Integer.TYPE) {
            object = 0;
        } else if (primitiveClass == Long.TYPE) {
            object = 0L;
        } else if (primitiveClass == Character.TYPE) {
            object = 'a';
        } else if (primitiveClass == Float.TYPE) {
            object = 0F;
        } else if (primitiveClass == Double.TYPE) {
            object = 0.0;
        } else if (primitiveClass == Boolean.TYPE) {
            object = false;
        }
        return object;
    }

}

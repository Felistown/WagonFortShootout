package WagonFortShootout.java.utils.mutable;

import java.util.HashMap;
import java.util.function.Consumer;

public class MutableConsumer<T> implements Consumer {
    //TODO this isnt safe, other method preferred


    public static final HashMap<Integer, MutableConsumer> CONSUMERS = new HashMap<Integer, MutableConsumer>();

    private Consumer<T> consumer;

   public static <T> Consumer<T> putKey(int key) {
       MutableConsumer<T> consumer = new MutableConsumer<T>(e -> {});
       CONSUMERS.put(key, consumer);
       return consumer;
   }

   public static <T> void set(int key, Consumer<T> consumer) {
       CONSUMERS.get(key).setConsumer(consumer);
       CONSUMERS.remove(key);
   }

   public MutableConsumer(Consumer<T> consumer) {
       this.consumer = consumer;
   }

   public void setConsumer(Consumer<T> consumer) {
       this.consumer = consumer;
   }

    @Override
    public void accept(Object o) {
        consumer.accept((T)o);
    }
}

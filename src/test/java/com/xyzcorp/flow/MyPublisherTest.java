package com.xyzcorp.flow;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow;

public class MyPublisherTest {
    @Test
    void testUsingPublishers() {
        ExecutorService executorService =
            Executors.newFixedThreadPool(10);


        MyPublisher myPublisher = new MyPublisher(executorService);

        Flow.Subscriber<Long> rocky = new Flow.Subscriber<>() {
            private Flow.Subscription subscription;

            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                this.subscription = subscription;
                this.subscription.request(1000);
            }

            @Override
            public void onNext(Long item) {
                if (item < 0) throw new RuntimeException("Nope");
                System.out.println("S1: (" +
                    Thread.currentThread().getName() + ")" + item);
                if (item == 300) {
                    this.subscription.cancel();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onComplete() {
                System.out.println("S1: Done!");
            }
        };
        myPublisher.subscribe(rocky);

        Flow.Subscriber<Long> shanshan = new Flow.Subscriber<>() {
            private Flow.Subscription subscription;

            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                this.subscription = subscription;
                this.subscription.request(1000);
            }

            @Override
            public void onNext(Long item) {
                if (item < 0) throw new RuntimeException("Nope");
                System.out.println("S2: (" +
                    Thread.currentThread().getName() + ")" + item);
                if (item == 910) {
                    this.subscription.cancel();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onComplete() {
                System.out.println("S1: Done!");
            }
        };
        myPublisher.subscribe(shanshan);
    }
}

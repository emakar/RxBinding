package com.jakewharton.rxbinding.support.v7.widget;

import android.support.v7.widget.RecyclerView;
import rx.Observable;
import rx.Subscriber;
import rx.android.MainThreadSubscription;

import static com.jakewharton.rxbinding.internal.Preconditions.checkUiThread;

final class RecyclerViewScrollEventOnSubscribe
    implements Observable.OnSubscribe<RecyclerViewScrollEvent> {
  final RecyclerView recyclerView;

  public RecyclerViewScrollEventOnSubscribe(RecyclerView recyclerView) {
    this.recyclerView = recyclerView;
  }

  @Override public void call(final Subscriber<? super RecyclerViewScrollEvent> subscriber) {
    checkUiThread();

    final RecyclerView.OnScrollListener listener = new RecyclerView.OnScrollListener() {
      @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(RecyclerViewScrollEvent.create(recyclerView, dx, dy));
        }
      }
    };
    recyclerView.addOnScrollListener(listener);

    subscriber.add(new MainThreadSubscription() {
      @Override protected void onUnsubscribe() {
        recyclerView.removeOnScrollListener(listener);
      }
    });
  }
}

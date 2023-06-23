// Copyright (c) VMware, Inc. 2023. All rights reserved.
// SPDX-License-Identifier: Apache-2.0
package dev.gemfire.caching.multisite.util;

import org.springframework.lang.NonNull;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Abstract utility class for {@link Thread Threading}.
*
 * @see java.lang.Thread
 * @see java.time.Duration
 * @since 1.3.0
 */
public abstract class ThreadUtils {

	@SuppressWarnings("all")
	public static void safeSleep(@NonNull Object lock, @NonNull Duration duration) {

		boolean interrupted = false;

		long timeout = System.currentTimeMillis() + duration.toMillis();

		while (System.currentTimeMillis() < timeout) {
			synchronized (lock) {
				try {
					lock.wait(duration.toMillis(), 0);
				}
				catch (InterruptedException ignore) {
					interrupted = true;
				}
			}
		}

		if (interrupted) {
			Thread.currentThread().interrupt();
		}
	}

	public static boolean waitFor(@NonNull Duration duration, @NonNull Condition condition) {
		return waitFor(duration, duration.toMillis() / 10L, condition);
	}

	@SuppressWarnings("all")
	public static boolean waitFor(Duration duration, long interval, Condition condition) {

		long timeout = System.currentTimeMillis() + duration.toMillis();

		try {
			while (!condition.evaluate() && System.currentTimeMillis() < timeout) {
				synchronized (condition) {
					TimeUnit.MILLISECONDS.timedWait(condition, interval);
				}
			}
		}
		catch (InterruptedException cause) {
			Thread.currentThread().interrupt();
		}

		return condition.evaluate();
	}

	@FunctionalInterface
	public interface Condition {
		boolean evaluate();
	}
}

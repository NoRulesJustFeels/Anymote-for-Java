/*
 * Copyright (C) 2012 ENTERTAILION, LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.entertailion.java.anymote.util;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Blocking thread for processing messages
 *
 */
public abstract class MessageThread extends Thread {
	private static int QUEUE_SIZE = 100;
    private ArrayBlockingQueue<Message> queue = new ArrayBlockingQueue<Message>(QUEUE_SIZE);
    private Timer timer = new Timer(); // for delaying messages
    private volatile boolean running = true;

    public MessageThread() {
    }
    
    /** 
     * Process messages in the blocking queue
     * @see java.lang.Thread#run()
     */
    public void run() {
    	while (running) {
    		try {
				Message message = queue.take();
				if (message!=null) {
					handleMessage(message);
				}
			} catch (InterruptedException e) {
				running = false;
			}
    	}
    	
    }
    
    /**
     * Terminate the thread
     */
    public void terminate() {
    	timer.cancel();
        running = false;
    }

    /**
     * Needs to be implemented by subclass to process each message
     * @param message
     */
    public abstract void handleMessage(Message message);

    public void removeMessage(int what) {
    	for(Message message:queue) {
    		if (message.what==what) {
    			queue.remove(message);
    		}
    	}
    }
    
    /**
     * Add a message to the queue
     * @param message
     */
    public void sendMessage(Message message) {
    	if (message!=null) {
	    	try {
				queue.put(message);
			} catch (InterruptedException e) {
			}
    	}
    }

    /**
     * Add a message without a payload to the queue
     * @param what
     */
    public void sendEmptyMessage(int what) {
    	Message message = Message.obtain();
    	message.what = what;
    	sendMessage(message);
    }
    
    /**
     * Add a message to the queue after a delay
     * @param message
     * @param delay
     */
    public void sendMessageDelayed(final Message message, long delay) {
    	if (message!=null) {
	    	TimerTask timerTask = new TimerTask() {
	
				@Override
				public void run() {
			    	try {
						queue.put(message);
					} catch (InterruptedException e) {
					}
				}
	    		
	    	};
	    	if (delay>0) {
	    		timer.schedule(timerTask, delay);
	    	} else {
	    		timerTask.run();
	    	}
    	}
    }
    
    /**
     * Add a message without a payload to the queue after a delay
     * @param what
     * @param delay
     */
    public void sendEmptyMessageDelayed(int what, long delay) {
    	Message message = Message.obtain();
    	message.what = what;
    	if (delay>0) {
    		sendMessageDelayed(message, delay);
    	} else {
    		sendMessage(message);
    	}
    }
    
    /**
     * Instantiate a message
     * 
     * @param what
     * @param object
     * @return
     */
    public Message obtainMessage(int what, Object object) {
    	Message message = Message.obtain();  // TODO object pooling
    	message.what = what;
    	message.obj = object;
    	return message;
    }
    
    /**
     * Instantiate a message
     * @param what
     * @return
     */
    public Message obtainMessage(int what) {
    	return obtainMessage(what, null);
    }
}
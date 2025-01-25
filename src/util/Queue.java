package util;

import util.Position;

public class Queue {
  private Position[] array;
  private int front;
  private int rear;
  private int size;
  private int capacity;

  public Queue(int capacity) {
    this.capacity = capacity;
    this.array = new Position[capacity];
    this.front = 0;
    this.rear = -1;
    this.size = 0;
  }

  public boolean isEmpty() { return size == 0; }

  public boolean isFull() { return size == capacity; }

  public void enqueue(Position node) {
    if (isFull()) return;
    rear = (rear + 1) % capacity;
    array[rear] = node;
    ++size;
  }

  public Position dequeue() {
    if (isEmpty()) return new Position(-1, -1);
    Position node = array[front];
    front = (front + 1) % capacity;
    --size;
    return node;
  }
}

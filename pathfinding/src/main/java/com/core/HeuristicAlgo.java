package com.core;

/**
 * Created by zhanghaojie on 2017/11/12.
 * 距离算法
 */
public enum HeuristicAlgo {

    // 曼哈顿距离
    MANHATTAN {

        @Override
        public double algo(Node a, Node b) {
            return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
        }
    },

    // 欧几里得距离
    EUCLIDEAN {

        @Override
        public double algo(Node a, Node b) {
            return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
        }
    },

    // 欧式距离
    OCTILE {

        @Override
        public double algo(Node a, Node b) {
            double f = Math.sqrt(2) - 1;
            double dx = Math.abs(a.x - b.x);
            double dy = Math.abs(a.y - b.y);
            return (dx < dy) ? f * dx + dy : f * dy + dx;
        }
    },

    //切比雪夫距离
    CHEBYSHEV {

        @Override
        public double algo(Node a, Node b) {
            return Math.max(Math.abs(a.x - b.x), Math.abs(a.y - b.y));
        }
    };

    public abstract double algo(Node a, Node b);

}

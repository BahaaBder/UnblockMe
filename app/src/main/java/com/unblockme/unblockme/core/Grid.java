package com.unblockme.unblockme.core;


import android.util.ArrayMap;
import android.util.Log;

import com.unblockme.unblockme.utils.Bound;
import com.unblockme.unblockme.utils.Dimension;
import com.unblockme.unblockme.utils.Orientation;
import com.unblockme.unblockme.utils.Position;

import java.util.ArrayList;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

/**
 * This class manages all the logic connects to a grid,
 * such as the placement of blocks,
 * Block displacement
 */
public class Grid extends Observable {

    public static final int GRID_SIZE = 6;
    public static final int MARKED_ID = -1;
    private int[][] items = new int[GRID_SIZE][GRID_SIZE];
    private Map<Integer, Block> blocks = new ArrayMap<>();
    //Array List of ObServers...
    private ArrayList<Observer> observers = new ArrayList<>();
    private int cnt = 1;
    private Bound lastBound;

    /**
     * Starts the block matrix with 0 (which expresses that the box is empty).
     */
    public Grid() {

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++)
                this.items[i][j] = 0;
        }
    }

    @Override
    public synchronized void addObserver(Observer o) {
        this.observers.add(o);
    }

    @Override
    public void notifyObservers(Object arg) {
        for (Observer o :
                this.observers) {
            o.update(this, arg);
        }
    }

    @Override
    public void notifyObservers() {
        this.notifyObservers(null);
    }

    /**
     * Print
     */
    public void printGrid() {
        for (int i = 0; i < GRID_SIZE; i++) {
            StringBuilder str = new StringBuilder();
            for (int j = 0; j < GRID_SIZE; j++)
                str.append(this.items[j][i]).append("\t");
            Log.i("Grid", str.toString());
        }
    }

    /**
     * Insert n block in the grid
     *
     * @param b
     * @return false if the box is occupied, true if not
     */
    public boolean put(Block b) {
        if ((this.getIdByPosition(b.getPosition()) != 0) || (!this.set(b, cnt)))
            return false;
        this.blocks.put(cnt++, b); // blocks this Map Of blocks.
        return true;
    }

    /**
     * Inserts a Mark block
     *
     * @return false if the box is occupied, true otherwise
     */
    // Marked Block this is a Red Block.
    public boolean putMarked() {
        Block b = new Block(new Dimension(1, 2), new Position(0, 2));
        if ((this.getIdByPosition(b.getPosition()) != 0) || (!this.set(b, MARKED_ID)))
            return false;
        this.blocks.put(MARKED_ID, b);
        return true;
    }

    /**
     * Attaches a block to one or more squares (depending on its size)
     * Concretely: Put the block ID in question in the corresponding boxes
     *
     * @param b
     * @param bid
     * @return
     */
    //A function that receives a particular block and tries to insert it into an item matrix
    private boolean set(Block b, int bid) {
        for (int i = b.getPosition().getX(); i < b.getDimension().getWidth() +
                b.getPosition().getX(); i++) {
            for (int j = b.getPosition().getY(); j < b.getDimension().getLength() +
                    b.getPosition().getY(); j++) {
                //If the domain is occupied..
                if (this.items[i][j] != 0) return false;
                //
                this.items[i][j] = bid;
            }
        }
        return true;
    }

    /**
     * Returns the ID of the present blocks in the grid
     *
     * @return
     */
    //Set Prevents duplication of item and it is different from List..
    public Set<Integer> getBlockIds() {
        return this.blocks.keySet();
    }


    public int getIdByPosition(Position p) {
        int i;
        try {
            i = this.items[p.getX()][p.getY()];
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            return 0;
        }
        return i;
    }

    public Block getBlockById(int id) {
        return this.blocks.get(id);
    }

    private boolean isEmpty(Position p) {
        return (this.getIdByPosition(p) != 0);
    }

    /**
     * Detaches a block from the grid; Put the block boxes back to zero
     *
     * @param b
     */
    private void unset(Block b) {
        for (int i = b.getPosition().getX(); i < b.getDimension().getWidth() + b.getPosition().getX(); i++) {
            for (int j = b.getPosition().getY(); j < b.getDimension().getLength() + b.getPosition().getY(); j++)
                this.items[i][j] = 0;
        }
    }

    public Bound getMoveBoundaries(int bid) {

        Block b = this.getBlockById(bid);
        Position p = b.getPosition();

        int Positive_walking = 0, Negative_walking = 0;
        if (b.getOrientation() == Orientation.HORIZONTAL) {
            // Walk to the left Side Boundaries
            Positive_walking = p.getX();
            for (int i = p.getX() + b.getDimension().getWidth(); i < GRID_SIZE; i++) {
                if ((this.items[i][p.getY()] != 0) && (this.items[i][p.getY()] != bid)) {
                    break;
                }
                Positive_walking++;
            }
            // Walk to the right Side Boundaries
            Negative_walking = p.getX();
            for (int i = p.getX() - 1; i >= 0; i--) {
                if ((this.items[i][p.getY()] != 0) && (this.items[i][p.getY()] != bid)) {
                    break;
                }
                Negative_walking--;
            }
        } else {

            // block Walk Down Boundaries..
            Positive_walking = p.getY();
            for (int i = p.getY() + b.getDimension().getLength(); i < GRID_SIZE; i++) {
                if ((this.items[p.getX()][i] != 0) && (this.items[p.getX()][i] != bid)) {
                    break;
                }
                Positive_walking++;
            }
            // block Walk Up Boundaries..
            Negative_walking = p.getY();
            for (int i = p.getY() - 1; i >= 0; i--) {
                if ((this.items[p.getX()][i] != 0) && (this.items[p.getX()][i] != bid)) {
                    break;
                }
                Negative_walking--;
            }

        }
        this.lastBound = new Bound(Positive_walking, Negative_walking);
        return this.lastBound;
    }

    public boolean isValidMove(int bid, Position p) {
        Block b = this.blocks.get(bid);

        if ((p.getX() < 0) || (p.getY() < 0)) return false;
        if ((p.getX() >= GRID_SIZE) || (p.getY() >= GRID_SIZE)) return false;

        assert b != null;
        if (b.getOrientation() == Orientation.HORIZONTAL) {
            if (p.getX() > this.lastBound.getHigh()) return false;
            return p.getX() >= this.lastBound.getLow();
        } else {
            if (p.getY() > this.lastBound.getHigh()) return false;
            return p.getY() >= this.lastBound.getLow();
        }
    }

    /**
     * Deplace a block whose id is bid from its position to p
     * Performs all verifications whether the block can be move or not
     *
     * @param bid
     * @param p
     * @return true if the block has been placed; false if not
     */
    public boolean move(int bid, Position p) {
        Block b = this.blocks.get(bid);
        Position position = b.getPosition();
        if (p.equals(position)) return false;
        if (!this.isValidMove(bid, p)) return false;
        this.unset(b);
        b.setPosition(p);
        this.set(b, bid);
        this.setChanged();
        this.notifyObservers(new Move(bid, position, p));
        this.printGrid();

        return true;
    }
//When a red block comes out of a square..
    public boolean isSolved() {
        Block b = this.getBlockById(Grid.MARKED_ID);
        boolean b1 = b.getPosition().getX() == 4;
        return b1;
    }
}

package cn.edu.pku.sei.util;

import java.io.Closeable;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import cn.edu.pku.sei.mapStructures.BorderSegment;
import cn.edu.pku.sei.mapStructures.Coordinate;
import cn.edu.pku.sei.mapStructures.HexDir;

/**
 * 
 * This class uses delegation to implement a decorator pattern. The resulting 
 * customized scanner
 * does nearly everything a normal scanner does, plus it 
 * gracefully handles certain larger structures important to scanning 
 * the terrain specification files used to describe maps displayable by
 * the TerrainBoard. 
 * 
 * The new methods are hasDir(), nextDir(),
 * hasCoordinate(), nextCoordinate, hasBorderSegment(), 
 * nextBorderSegment();
 * 
 * @author Robert Ward
 *
 */
public class TerrainScanner implements Closeable, AutoCloseable, Iterator<String> {

    private Scanner in;
    
    public TerrainScanner(Readable reader){
        this.in = new Scanner(reader);
    }
    
    public TerrainScanner(String text){
        in = new Scanner(text);
    }
    
    private String dirPattern = "up|dn|ur|ul|dr|dl";
    public boolean hasNextDir(){
        return in.hasNext(dirPattern);
    }
    
    public HexDir nextDir(){
        String dir = in.next(dirPattern);
        return HexDir.fromAbbrev(dir);
    }
    
    private String coordPattern = "\\s*\\d+\\s+\\d+\\s*.*";
    private String patDelimiter = "[^\\s\\d\\w]";
    public boolean hasNextCoord(){
        return hasStructureHelper(coordPattern);
    }
    
    private boolean hasStructureHelper(String structure){
        Pattern dlm = in.delimiter();
        boolean rval = false;
        try {
            in.useDelimiter(patDelimiter);
            rval = in.hasNext(structure);
        } catch (Exception e){}
        finally {
            in.useDelimiter(dlm);
        }
        return rval;        
    }
    
    public Coordinate nextCoord(){
        if (!hasNextCoord()){
            throw new NoSuchElementException();
        }
        int x = in.nextInt();
        int y = in.nextInt();
        return new Coordinate(x,y);
    }
    
    private String borderPattern = "\\s*\\d+\\s+\\d+\\s*(["+dirPattern+"]\\s+){0,6}?.*";
    public boolean hasNextBorderSegment(){
        return hasStructureHelper(borderPattern);
    }
    
    public BorderSegment nextBorderSegment(){
        if (!hasNextBorderSegment()){
            throw new NoSuchElementException();
        }
        Coordinate coord = nextCoord();
        BorderSegment rval = new BorderSegment(coord);
        while (hasNextDir()){
            rval.add(nextDir());
        }
        return rval;
    }
    
    /**
     * 
     * @see java.util.Scanner#close()
     */
    public void close() {
        in.close();
    }

    /**
     * @return
     * @see java.util.Scanner#delimiter()
     */
    public Pattern delimiter() {
        return in.delimiter();
    }

    /**
     * @param obj
     * @return
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        return in.equals(obj);
    }

    /**
     * @param pattern
     * @return
     * @see java.util.Scanner#findInLine(java.util.regex.Pattern)
     */
    public String findInLine(Pattern pattern) {
        return in.findInLine(pattern);
    }

    /**
     * @param pattern
     * @return
     * @see java.util.Scanner#findInLine(java.lang.String)
     */
    public String findInLine(String pattern) {
        return in.findInLine(pattern);
    }

    /**
     * @param pattern
     * @param horizon
     * @return
     * @see java.util.Scanner#findWithinHorizon(java.util.regex.Pattern, int)
     */
    public String findWithinHorizon(Pattern pattern, int horizon) {
        return in.findWithinHorizon(pattern, horizon);
    }

    /**
     * @param pattern
     * @param horizon
     * @return
     * @see java.util.Scanner#findWithinHorizon(java.lang.String, int)
     */
    public String findWithinHorizon(String pattern, int horizon) {
        return in.findWithinHorizon(pattern, horizon);
    }

    /**
     * @return
     * @see java.util.Scanner#hasNext()
     */
    public boolean hasNext() {
        return in.hasNext();
    }

    /**
     * @param pattern
     * @return
     * @see java.util.Scanner#hasNext(java.util.regex.Pattern)
     */
    public boolean hasNext(Pattern pattern) {
        return in.hasNext(pattern);
    }

    /**
     * @param pattern
     * @return
     * @see java.util.Scanner#hasNext(java.lang.String)
     */
    public boolean hasNext(String pattern) {
        return in.hasNext(pattern);
    }

    /**
     * @return
     * @see java.util.Scanner#hasNextBigDecimal()
     */
    public boolean hasNextBigDecimal() {
        return in.hasNextBigDecimal();
    }

    /**
     * @return
     * @see java.util.Scanner#hasNextBigInteger()
     */
    public boolean hasNextBigInteger() {
        return in.hasNextBigInteger();
    }

    /**
     * @param radix
     * @return
     * @see java.util.Scanner#hasNextBigInteger(int)
     */
    public boolean hasNextBigInteger(int radix) {
        return in.hasNextBigInteger(radix);
    }

    /**
     * @return
     * @see java.util.Scanner#hasNextBoolean()
     */
    public boolean hasNextBoolean() {
        return in.hasNextBoolean();
    }

    /**
     * @return
     * @see java.util.Scanner#hasNextByte()
     */
    public boolean hasNextByte() {
        return in.hasNextByte();
    }

    /**
     * @param radix
     * @return
     * @see java.util.Scanner#hasNextByte(int)
     */
    public boolean hasNextByte(int radix) {
        return in.hasNextByte(radix);
    }

    /**
     * @return
     * @see java.util.Scanner#hasNextDouble()
     */
    public boolean hasNextDouble() {
        return in.hasNextDouble();
    }

    /**
     * @return
     * @see java.util.Scanner#hasNextFloat()
     */
    public boolean hasNextFloat() {
        return in.hasNextFloat();
    }

    /**
     * @return
     * @see java.util.Scanner#hasNextInt()
     */
    public boolean hasNextInt() {
        return in.hasNextInt();
    }

    /**
     * @param radix
     * @return
     * @see java.util.Scanner#hasNextInt(int)
     */
    public boolean hasNextInt(int radix) {
        return in.hasNextInt(radix);
    }

    /**
     * @return
     * @see java.util.Scanner#hasNextLine()
     */
    public boolean hasNextLine() {
        return in.hasNextLine();
    }

    /**
     * @return
     * @see java.util.Scanner#hasNextLong()
     */
    public boolean hasNextLong() {
        return in.hasNextLong();
    }

    /**
     * @param radix
     * @return
     * @see java.util.Scanner#hasNextLong(int)
     */
    public boolean hasNextLong(int radix) {
        return in.hasNextLong(radix);
    }

    /**
     * @return
     * @see java.util.Scanner#hasNextShort()
     */
    public boolean hasNextShort() {
        return in.hasNextShort();
    }

    /**
     * @param radix
     * @return
     * @see java.util.Scanner#hasNextShort(int)
     */
    public boolean hasNextShort(int radix) {
        return in.hasNextShort(radix);
    }

    /**
     * @return
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return in.hashCode();
    }

    /**
     * @return
     * @see java.util.Scanner#ioException()
     */
    public IOException ioException() {
        return in.ioException();
    }

    /**
     * @return
     * @see java.util.Scanner#locale()
     */
    public Locale locale() {
        return in.locale();
    }

    /**
     * @return
     * @see java.util.Scanner#match()
     */
    public MatchResult match() {
        return in.match();
    }

    /**
     * @return
     * @see java.util.Scanner#next()
     */
    public String next() {
        return in.next();
    }

    /**
     * @param pattern
     * @return
     * @see java.util.Scanner#next(java.util.regex.Pattern)
     */
    public String next(Pattern pattern) {
        return in.next(pattern);
    }

    /**
     * @param pattern
     * @return
     * @see java.util.Scanner#next(java.lang.String)
     */
    public String next(String pattern) {
        return in.next(pattern);
    }

    /**
     * @return
     * @see java.util.Scanner#nextBigDecimal()
     */
    public BigDecimal nextBigDecimal() {
        return in.nextBigDecimal();
    }

    /**
     * @return
     * @see java.util.Scanner#nextBigInteger()
     */
    public BigInteger nextBigInteger() {
        return in.nextBigInteger();
    }

    /**
     * @param radix
     * @return
     * @see java.util.Scanner#nextBigInteger(int)
     */
    public BigInteger nextBigInteger(int radix) {
        return in.nextBigInteger(radix);
    }

    /**
     * @return
     * @see java.util.Scanner#nextBoolean()
     */
    public boolean nextBoolean() {
        return in.nextBoolean();
    }

    /**
     * @return
     * @see java.util.Scanner#nextByte()
     */
    public byte nextByte() {
        return in.nextByte();
    }

    /**
     * @param radix
     * @return
     * @see java.util.Scanner#nextByte(int)
     */
    public byte nextByte(int radix) {
        return in.nextByte(radix);
    }

    /**
     * @return
     * @see java.util.Scanner#nextDouble()
     */
    public double nextDouble() {
        return in.nextDouble();
    }

    /**
     * @return
     * @see java.util.Scanner#nextFloat()
     */
    public float nextFloat() {
        return in.nextFloat();
    }

    /**
     * @return
     * @see java.util.Scanner#nextInt()
     */
    public int nextInt() {
        return in.nextInt();
    }

    /**
     * @param radix
     * @return
     * @see java.util.Scanner#nextInt(int)
     */
    public int nextInt(int radix) {
        return in.nextInt(radix);
    }

    /**
     * @return
     * @see java.util.Scanner#nextLine()
     */
    public String nextLine() {
        return in.nextLine();
    }

    /**
     * @return
     * @see java.util.Scanner#nextLong()
     */
    public long nextLong() {
        return in.nextLong();
    }

    /**
     * @param radix
     * @return
     * @see java.util.Scanner#nextLong(int)
     */
    public long nextLong(int radix) {
        return in.nextLong(radix);
    }

    /**
     * @return
     * @see java.util.Scanner#nextShort()
     */
    public short nextShort() {
        return in.nextShort();
    }

    /**
     * @param radix
     * @return
     * @see java.util.Scanner#nextShort(int)
     */
    public short nextShort(int radix) {
        return in.nextShort(radix);
    }

    /**
     * @return
     * @see java.util.Scanner#radix()
     */
    public int radix() {
        return in.radix();
    }

    /**
     * 
     * @see java.util.Scanner#remove()
     */
    public void remove() {
        in.remove();
    }

    /**
     * @return
     * @see java.util.Scanner#reset()
     */
    public Scanner reset() {
        return in.reset();
    }

    /**
     * @param pattern
     * @return
     * @see java.util.Scanner#skip(java.util.regex.Pattern)
     */
    public Scanner skip(Pattern pattern) {
        return in.skip(pattern);
    }

    /**
     * @param pattern
     * @return
     * @see java.util.Scanner#skip(java.lang.String)
     */
    public Scanner skip(String pattern) {
        return in.skip(pattern);
    }

    /**
     * @return
     * @see java.util.Scanner#toString()
     */
    public String toString() {
        return in.toString();
    }

    /**
     * @param locale
     * @return
     * @see java.util.Scanner#useLocale(java.util.Locale)
     */
    public Scanner useLocale(Locale locale) {
        return in.useLocale(locale);
    }

    /**
     * @param radix
     * @return
     * @see java.util.Scanner#useRadix(int)
     */
    public Scanner useRadix(int radix) {
        return in.useRadix(radix);
    }

    

}

package shop.data;

/**
 * Implementation of Video interface.
 * @see Data
 */
final class VideoObj implements Video {
  private final String _title;
  private final int    _year;
  private final String _director;

  /**
   * Initialize all object attributes.
   * Title and director are "trimmed" to remove leading and final space.
   * @throws IllegalArgumentException if object invariant violated.
   */
  VideoObj(String title, int year, String director) {
    if(title == null || director== null)
      throw new IllegalArgumentException("Director and title must not be null");
    if(title.trim().equals("") || director.trim().equals(""))
      throw new IllegalArgumentException("Director and title must not be non-empty");
    if(!(year > 1800 && year < 5000))
      throw new IllegalArgumentException("Year must be between 1800 and 5000");
    _title = title.trim();
    _year = year;
    _director = director.trim();
  }

  public String director() {

    return _director;
  }

  public String title() {

    return _title;
  }

  public int year() {

    return _year;
  }

  public boolean equals(Object thatObject) {
    if(this==thatObject) return true;
    if(thatObject==null) return false;
    if(!(thatObject instanceof Video)) return false;

    Video that = (VideoObj) thatObject;
    if(!(_director.equals(that.director()))) return false;

    if(! _title.equals(that.title())) return false;

    if( _year != that.year() ) return false;

    return true;
  }

  public int hashCode() {
    int result=17;
    result = 37*result +_title.hashCode();
    result = 37*result + _year;
    result = 37*result+_director.hashCode();
    return result;
  }

  public int compareTo(Video that) {
    if(!_title.equals(that.title())) return _title.compareTo(that.title());
    else if(_year != that.year()) return _year-that.year();
    else if(!_director.equals(that.director())) return _director.compareTo(that.director());
    else return 0;
  }

  public String toString() {

    return String.format("%s (%d) : %s", _title, _year, _director);
  }
}

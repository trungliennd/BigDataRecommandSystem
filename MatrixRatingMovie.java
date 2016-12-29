package data.movive;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

class Pair {
	int movie_id;
	float ratingofMovie;
	
	public Pair(int a,float b) {
		movie_id = a;
		ratingofMovie  = b;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder strings = new StringBuilder();
		strings.append("(" + movie_id + ", " + ratingofMovie + ")" );
		return strings.toString();
	}
}


public class MatrixRatingMovie {

	private int n_user = 138492;
	private int m_movie = 27278;
	private Map<Integer, ArrayList<Pair>> user_movies;
	private Map<Integer,Integer> movieId_index;
	private float matrix_user_movie[];
	
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	
	public MatrixRatingMovie(){
		user_movies = new HashMap<Integer, ArrayList<Pair>>();
		movieId_index = new HashMap<>();
		matrix_user_movie = new float[m_movie];
		/*for(int i = 0;i < n_user;i++){
			for(int j = 0;j < m_movie;j++) {
				matrix_user_movie[i][j] = new Float(0);
			}
		}*/
	}
	public void init() {
		for(int i = 0;i < m_movie;i++) {
			matrix_user_movie[i] = 0;
		}
	}
	/*/
	 * read matrix thua
	 */
	public void readFileRating(String filename) throws IOException {
		bufferedReader = new BufferedReader(new FileReader(new File(filename)));
		bufferedReader.readLine();
		//int count = 0;
		String line = bufferedReader.readLine();
		while(line != null) {
			String line_index[] = line.split(",");
			Integer user_id = Integer.parseInt(line_index[0]);
			Pair movieAndRating = new Pair(Integer.parseInt(line_index[1]),Float.parseFloat(line_index[2]));
			if(user_movies.containsKey(user_id)) {
				user_movies.get(user_id).add(movieAndRating);
			}else {
				ArrayList<Pair> list = new ArrayList<Pair>();
				list.add(movieAndRating);
				user_movies.put(user_id, list);
			}
			line = bufferedReader.readLine();
			System.out.println("Running ...");
		}
		bufferedReader.close();
		System.out.println("READ FILE DONE!");
	}
	
	public void readFileMoives(String filename) throws IOException{
		bufferedReader = new BufferedReader(new FileReader(new File(filename)));
		String line = bufferedReader.readLine();
		int count = 0;
		while(line != null) {
			String line_one[] = line.split("\t");
			movieId_index.put(Integer.parseInt(line_one[0]), count);
			count++;
			line = bufferedReader.readLine();
		}
		bufferedReader.close();
	}
	
	public void readInitMatrix(String filename) throws IOException {
		readFileMoives("Data/Movies_Title.txt");
		bufferedReader = new BufferedReader(new FileReader(new File(filename)));
		bufferedReader.readLine();
		String line = bufferedReader.readLine();
		while(line != null) {
			String strings[] = line.split(",");
			Integer user_id = Integer.parseInt(strings[0]);
			Integer movie_id = movieId_index.get(Integer.parseInt(strings[1]));
			
			line = bufferedReader.readLine();
		}
		bufferedReader.close();
	}
	
	public void countUser(String filename) throws IOException {
		bufferedReader = new BufferedReader(new FileReader(new File(filename)));
		bufferedReader.readLine();
		String line = bufferedReader.readLine();
		int count = 0;
		int user_id = 1;
		while(line != null) {
			String strings[] = line.split(",");
			if(user_id != Integer.parseInt(strings[0])){
				user_id = Integer.parseInt(strings[0]);
//				System.out.println("count is: " + "(" +user_id +", "+ count+")");
				count++;
			}
			line = bufferedReader.readLine();
		}
		System.out.println("user is: " + count);
		bufferedReader.close();
	}
	
	public void sortUserId() {
		List<Map.Entry<Integer, ArrayList<Pair>>> list =  new LinkedList<>(user_movies.entrySet());
		Collections.sort(list,new Comparator<Map.Entry<Integer,
				ArrayList<Pair>>>() {
			@Override
			public int compare(Entry<Integer, ArrayList<Pair>> o1,
					Entry<Integer, ArrayList<Pair>> o2) {
				// TODO Auto-generated method stub
				return (o1.getKey()).compareTo(o2.getKey());
			}
		});
		user_movies.clear();
		user_movies = new LinkedHashMap<>();
		for (Map.Entry<Integer, ArrayList<Pair>> entry : list) {
            user_movies.put(entry.getKey(), entry.getValue());
	    }
		System.out.println("SORTED DONE!");
	}
	
	public void writeMatrix(String filename) throws IOException {
		sortUserId();
		bufferedWriter = new BufferedWriter(new FileWriter(new File(filename)));
		StringBuilder strings = new StringBuilder();
		Set<Map.Entry<Integer, ArrayList<Pair>>> set = user_movies.entrySet();
		for(Map.Entry<Integer, ArrayList<Pair>> rate:set) {
			init();
			ArrayList<Pair> list = rate.getValue();
			int size = list.size();
			for(int i = 0;i < size;i++) {
				int movie_id = movieId_index.get(list.get(i).movie_id);
				matrix_user_movie[movie_id] = list.get(i).ratingofMovie;
			}
			for(int i = 0;i < m_movie;i++) {
				strings.append(matrix_user_movie[i]);
				strings.append(" ");
			}
			strings.append("\n");
			bufferedWriter.write(strings.toString());
			strings.delete(0, strings.length());
		}
		System.out.println("WRITE FILE DONE!");
	}
	
	public void check(String filename) throws IOException {
		bufferedReader = new BufferedReader(new FileReader(new File(filename)));
		String line = bufferedReader.readLine();
		String rate[] = line.split(" ");
		int size = rate.length;
		for(int i = 0;i < size;i++) {
			if(Float.parseFloat(rate[i])!= 0.0) {
				System.out.println("(" + i + ", " + rate[i]+")");
			}
		}
		bufferedReader.close();
	}
	/*
	 * (1, 3.5)
(28, 3.5)
(31, 3.5)
(46, 3.5)
(49, 3.5)
(110, 3.5)
(149, 4.0)
(220, 4.0)
(250, 4.0)
(257, 4.0)
(290, 4.0)
(293, 4.0)
(315, 4.0)
(333, 3.5)
(363, 3.5)
(537, 4.0)
(583, 3.5)
(587, 3.5)
(645, 3.0)
(902, 3.5)
(907, 3.5)
(990, 3.5)
(1017, 4.0)
(1057, 4.0)
(1058, 3.5)
(1067, 3.5)
(1068, 4.0)
(1075, 4.0)
(1113, 3.5)
(1169, 3.5)
(1171, 4.5)
(1173, 4.5)
(1175, 4.0)
(1176, 3.0)
(1182, 3.5)
(1188, 4.0)
(1189, 4.0)
(1191, 3.5)
(1193, 4.0)
(1196, 3.5)
(1212, 4.0)
(1215, 3.0)
(1218, 3.5)
(1221, 4.0)
(1230, 4.0)
(1231, 4.0)
(1233, 3.5)
(1234, 3.5)
(1238, 4.0)
(1250, 4.0)
(1263, 3.5)
(1275, 3.0)
(1292, 4.0)
(1304, 4.0)
(1318, 3.5)
(1320, 3.5)
(1328, 4.0)
(1339, 3.0)
(1343, 4.0)
(1356, 4.0)
(1478, 3.0)
(1532, 3.5)
(1686, 3.5)
(1765, 3.5)
(1836, 3.5)
(1883, 4.0)
(1910, 3.5)
(1913, 3.5)
(1937, 4.0)
(2016, 4.0)
(2034, 4.0)
(2054, 4.0)
(2056, 4.0)
(2059, 4.0)
(2089, 4.0)
(2090, 4.0)
(2109, 4.0)
(2110, 3.5)
(2168, 3.5)
(2203, 4.0)
(2206, 4.0)
(2457, 4.0)
(2543, 4.0)
(2559, 3.5)
(2562, 3.5)
(2578, 3.5)
(2597, 3.5)
(2606, 3.5)
(2630, 3.5)
(2675, 3.0)
(2676, 4.0)
(2718, 3.5)
(2786, 4.0)
(2832, 3.5)
(2858, 4.0)
(2861, 3.5)
(2873, 4.0)
(2882, 4.0)
(2914, 3.5)
(2943, 3.0)
(2950, 3.5)
(2994, 4.0)
(3066, 4.0)
(3178, 3.5)
(3349, 3.5)
(3387, 3.5)
(3390, 4.0)
(3399, 4.0)
(3409, 4.0)
(3796, 4.0)
(3839, 3.0)
(3902, 4.0)
(3903, 3.5)
(3917, 4.0)
(3933, 4.0)
(4011, 3.5)
(4034, 4.0)
(4039, 3.0)
(4132, 3.5)
(4211, 4.0)
(4351, 3.5)
(4372, 4.0)
(4476, 4.0)
(4625, 3.5)
(4658, 4.0)
(4782, 3.5)
(4800, 4.0)
(4815, 4.0)
(4819, 3.0)
(4845, 3.5)
(4884, 3.5)
(4897, 5.0)
(4930, 4.0)
(4943, 4.0)
(4944, 3.0)
(5050, 3.5)
(5075, 4.0)
(5443, 4.0)
(5580, 3.5)
(5698, 4.0)
(5717, 4.0)
(5799, 3.5)
(5853, 5.0)
(5900, 3.5)
(5994, 4.0)
(6143, 3.5)
(6234, 4.0)
(6392, 3.5)
(6429, 4.0)
(6644, 4.0)
(6645, 3.5)
(6664, 4.0)
(6697, 3.5)
(6724, 3.5)
(6778, 3.0)
(6889, 3.5)
(6933, 3.5)
(6934, 4.0)
(7041, 5.0)
(7052, 3.5)
(7135, 3.5)
(7275, 3.5)
(7277, 4.0)
(7312, 4.0)
(7323, 3.5)
(7328, 4.0)
(7344, 3.0)
(7455, 4.0)
(7769, 4.0)
(7839, 3.5)
(7860, 5.0)
(7953, 4.5)
(8007, 3.5)
(8278, 4.0)
(9757, 4.0)

	 */
	public static void main(String args[]) throws IOException {
		MatrixRatingMovie movie = new MatrixRatingMovie();
		movie.readFileRating("ml-20m/ratings.csv");
		movie.readFileMoives("Data/Movies_Title.txt");
		movie.writeMatrix("Data/Movie_ratings.txt");
		movie.check("Data/Movie_ratings.txt");
	}

	public int getN_user() {
		return n_user;
	}

	public void setN_user(int n_user) {
		this.n_user = n_user;
	}

	public int getM_movie() {
		return m_movie;
	}

	public void setM_movie(int m_movie) {
		this.m_movie = m_movie;
	}

	public Map<Integer, ArrayList<Pair>> getUser_movies() {
		return user_movies;
	}

	public void setUser_movies(Map<Integer, ArrayList<Pair>> user_movies) {
		this.user_movies = user_movies;
	}

	/*public float[][] getMatrix_user_movie() {
		return matrix_user_movie;
	}

	public void setMatrix_user_movie(float[][] matrix_user_movie) {
		this.matrix_user_movie = matrix_user_movie;
	}*/

	public BufferedReader getBufferedReader() {
		return bufferedReader;
	}

	public void setBufferedReader(BufferedReader bufferedReader) {
		this.bufferedReader = bufferedReader;
	}

	public BufferedWriter getBufferedWriter() {
		return bufferedWriter;
	}

	public void setBufferedWriter(BufferedWriter bufferedWriter) {
		this.bufferedWriter = bufferedWriter;
	}
	
}

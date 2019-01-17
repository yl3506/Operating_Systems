import java.io.*;
import java.util.*;

public class Module{
	private int module_num = 0;
	private int start_add = 0;
	private int module_len = 0;
	private Hashtable<Integer, String> use_table = new Hashtable<>();
	private ArrayList<Integer> text_list = new ArrayList<>();
	private ArrayList<String> result = new ArrayList<>();
	private Hashtable<Integer, Integer> mult_ref_table = new Hashtable<>();

	public Module(){}
	public Module(int n){
		this.module_num = n;
	}
	public void set_mod_num(int n){
		this.module_num = n;
	}
	public void add_mult_ref(int i, int b){
		this.mult_ref_table.put(i, b);
	}
	public Integer check_mult_ref(int i){
		return this.mult_ref_table.get(i);
	}
	public void add_use(String v, ArrayList<Integer> list){
		for (int i = 0; i < list.size(); i++){
			this.use_table.put(list.get(i), v);
		}
	}
	public void add_text(int text){
		this.text_list.add(text);
	}
	public ArrayList<Integer> get_text_list(){
		return this.text_list;
	}
	public String get_use(int k){
		return this.use_table.get(k);
	}
	public int get_start(){
		return this.start_add;
	}
	public int get_len(){
		return this.module_len;
	}
	public void set_start(int i){
		this.start_add = i;
	}
	public void set_len(int i){
		this.module_len = i;
	}
	public void add_result(String r){
		this.result.add(r);
	}
	public ArrayList<String> get_result(){
		return this.result;
	}
	public int get_module_num(){
		return module_num;
	}
}
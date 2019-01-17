import java.io.*;
import java.util.*;

public class Var{
	private String var_name = "";
	private int abs_add = 0;
	private boolean used = false;
	private boolean mult_def = false;
	private Module module = null;
	private boolean exceed_size = false;

	public Var(){}
	public Var(String name, int add, Module m){
		this.var_name = name;
		this.abs_add = add;
		this.module = m;
	}
	public void set_abs_add(int i){
		this.abs_add = i;
	}
	public void set_mod(Module m){
		this.module = m;
	}
	public String get_name(){
		return this.var_name;
	}
	public int get_abs_add(){
		return this.abs_add;
	}
	public void set_mult_def(){
		this.mult_def = true;
	}
	public void set_used(){
		this.used = true;
	}
	public boolean check_used(){
		return this.used;
	}
	public boolean get_mult_def(){
		return this.mult_def;
	}
	public void check_var_size(){
		Module mod = this.module;
		int mod_len = mod.get_len();
		int mod_start = mod.get_start();
		if(abs_add >= mod_len + mod_start){
			this.exceed_size = true;
			this.abs_add = mod_start + mod_len - 1;
		}
	}
	public boolean get_exceed_size(){
		return this.exceed_size;
	}
	public Module get_module(){
		return this.module;
	}
	public boolean get_used(){
		return this.used;
	}
	public boolean too_long(int max){
		if (this.var_name.length() > max){
			return true;
		}
		return false;
	}
}

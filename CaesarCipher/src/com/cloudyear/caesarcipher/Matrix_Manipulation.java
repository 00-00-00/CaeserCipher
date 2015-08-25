package com.cloudyear.caesarcipher;

public class Matrix_Manipulation {
	
	
	public static int[] multiply(int mat[][],int[] mat2,int si){
	       int [] sol;
	       sol = new int[si];
	       for(int i=0;i<si;i++){
	               sol[i]=0;
	               for(int j=0;j<si;j++){
	                   sol[i]+=(mat[i][j]*mat2[j]);
	               }
	       }
	       return sol;
	    }
	public static int[] mod(int mat[],int v,int si)
	{
		for(int i=0;i<si;i++)
		{
			mat[i]=mat[i]%v;
		}
		return mat;
	}
	
	public int adjoint(int a[][],int si)
	{
		int adj=0;
		
		for(int i=0;i<si;i++)
		{
			
			for(int j=0;j<si;j++)
			{
				adj+=(-1^(i+j))*(a[i][j]*((a[(i+1)%3][(j+1)%3])*a[(i+2)%3][(j+2)%3])-(a[(i+2)%3][(j+1)%3]*a[(i+1)%3][(j+2)%3]));
			}
		}
		return adj;
		
	}
	public int[][] transpose(int a[][],int si)
	{
		int [][]b=new int[si][si];
		for(int i=0;i<si;i++)
		{
			
			for(int j=0;j<si;j++)
			{
				b[j][i]=a[i][j];
			}
		}
		return b;
	}
	

}

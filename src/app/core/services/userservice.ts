import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class Userservice {
  private baseUrl = 'http://localhost:9090/users';
  constructor(private http: HttpClient) { }

  
  getUserById(id: number): Observable<any> {
  return this.http.get(`${this.baseUrl}/${id}`);
}

updateUser(id: number, userData: any, imageFile: File | null): Observable<any> {
  const formData = new FormData();

  if (imageFile) {
    formData.append('file', imageFile); // ✅ must match @RequestPart("file")
  }

  const userBlob = new Blob([JSON.stringify(userData)], { type: 'application/json' });
  formData.append('user', userBlob); // ✅ must match @RequestPart("user")

  return this.http.put(`${this.baseUrl}/update/${id}`, formData);
}


saveUser(userData: any,action : string): Observable<any> {
  const formData = new FormData();

  // // Attach image file if selected
  // if (imageFile) {
  //   formData.append('file', imageFile); // backend expects 'file'
  // }

  // // Attach user DTO as JSON blob
  // const userDtoBlob = new Blob([JSON.stringify(userData)], { type: 'application/json' });
  // formData.append('userDto'.trim(), userDtoBlob); // backend expects 'userDto'

  // return this.http.post(`${this.baseUrl}/saveOrUpdateUser?action=${action}`,userData);
   return this.http.post(`${this.baseUrl}/saveOrUpdateUser?action=${action}`, userData, {
    responseType: 'text' as 'json'  // 🔥 critical fix
  });
}


getAllUsers(): Observable<any[]> {
  return this.http.get<any[]>(`${this.baseUrl}/all`);
}

deleteUser(id: number): Observable<void> {
  return this.http.delete<void>(`${this.baseUrl}/delete/${id}`);
}

getUsers(startIndex:Number,lastIndex:Number): Observable<any[]> {
  return this.http.get<any[]>(`${this.baseUrl}/getByUsers/${startIndex}/${lastIndex}`);

}

getUsersCount(): Observable<number> {
  return this.http.get<number>(`${this.baseUrl}/getByUserCount`);
}
}

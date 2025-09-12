import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { count } from 'node:console';

@Injectable({
  providedIn: 'root'
})
export class Locationservice 
{

  private locationsUrl = 'assets/location-data.json';
  private locationData: any[] = [];

   private baseUrl = 'http://localhost:9090/location';


  constructor(private http: HttpClient) {}

  private loadLocationData(): Observable<any[]> {
    if (this.locationData.length > 0) {
      return of(this.locationData);
    } else {
      return this.http.get<any[]>(this.locationsUrl).pipe(
        map((data) => {
          this.locationData = data;
          return this.locationData; 
        })
      );
    }
  }

  getCountries(): Observable<{ code: string; name: string }[]> {
    return this.loadLocationData().pipe(
      map((data) => data.map(country => ({
        code: country.countryCode,
        name: country.countryName
      })))
    ); 
  }

 getStates(countryName: string): Observable<string[]> {
  return this.loadLocationData().pipe(
    map((data) => {
      const country = data.find(c => 
        c.countryName.toLowerCase() === countryName.toLowerCase()
      );
      return country ? country.states.map((s: any) => s.stateName) : [];
    })
  );
}

getDistricts(countryName: string, stateName: string): Observable<string[]> {
  return this.loadLocationData().pipe(
    map((data) => {
      const country = data.find(c => 
        c.countryName.toLowerCase() === countryName.toLowerCase()
      );
      const state = country?.states.find((s: any) => 
        s.stateName.toLowerCase() === stateName.toLowerCase()
      );
      return state ? state.districts.map((d: any) => d.districtName) : [];
    })
  );
}


  getPincode(countryName: string, stateName: string, districtName: string): Observable<string | null> {
  return this.loadLocationData().pipe(
    map((data) => {
      const country = data.find(c => 
        c.countryName.toLowerCase() === countryName.toLowerCase()
      );
      const state = country?.states.find((s: any) => 
        s.stateName.toLowerCase() === stateName.toLowerCase()
      );
      const district = state?.districts.find((d: any) => 
        d.districtName.toLowerCase() === districtName.toLowerCase()
      );
      return district ? district.pincode : null;
    })
  );
}


  getCountryFullName(code: string): Observable<string> {
    return this.loadLocationData().pipe(
      map((data) => {
        const country = data.find(c => c.countryCode === code);
        return country?.countryName || code;
      })
    );
  }


getAllCountries(): Observable<any[]> {
  return this.http.get<{ countries: any[] }>(`${this.baseUrl}/countries`).pipe(
    map(res => res.countries || [])
  );
}

getStatesByCountry(countryId: number): Observable<any[]> {
  return this.http.get<{ stateDtos: any[] }>(`${this.baseUrl}/countries/${countryId}/states`).pipe(
    map(res => (res.stateDtos || []).map(s => ({ id: s.id, name: s.stateName })))
  );
}


getDistrictsByState(stateId: number): Observable<any[]> {
  return this.http.get<any[]>(`${this.baseUrl}/states/${stateId}/districts`).pipe(
    map(districts =>
      districts.map(d => ({
        id: d.id,
        name: d.districtName || d.name,   // ✅ works for both cases
        pincode: d.pinCode?.code || null  // ✅ extract nested pin code
      }))
    )
  );
}


getPincodeByDistrict(districtId: number): Observable<{ code: string }> {
  return this.http.get<{ code: string }>(`${this.baseUrl}/districts/${districtId}/pincode`)
    .pipe(
      catchError(err => {
        console.error(`Failed to fetch pincode for district ${districtId}`, err);
        return throwError(() => new Error('Could not fetch pincode'));
      })
    );
}





}

export interface Country {
  id: number;
  name: string;
}

export interface State {
  id: number;
  stateName: string;
}

export interface District {
  id: number;
  districtName: string;
}

export interface PinCode {
  code: string;
}


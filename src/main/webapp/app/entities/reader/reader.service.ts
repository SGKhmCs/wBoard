import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Reader } from './reader.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class ReaderService {

    private resourceUrl = 'api/readers';
    private resourceSearchUrl = 'api/_search/readers';

    constructor(private http: Http) { }

    create(reader: Reader): Observable<Reader> {
        const copy = this.convert(reader);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(reader: Reader): Observable<Reader> {
        const copy = this.convert(reader);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<Reader> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        return new ResponseWrapper(res.headers, jsonResponse);
    }

    private convert(reader: Reader): Reader {
        const copy: Reader = Object.assign({}, reader);
        return copy;
    }
}

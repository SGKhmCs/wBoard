import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { BoardUser } from './board-user.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class BoardUserService {

    private resourceUrl = 'api/board-users';
    private resourceSearchUrl = 'api/_search/board-users';

    constructor(private http: Http) { }

    create(boardUser: BoardUser): Observable<BoardUser> {
        const copy = this.convert(boardUser);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(boardUser: BoardUser): Observable<BoardUser> {
        const copy = this.convert(boardUser);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<BoardUser> {
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

    private convert(boardUser: BoardUser): BoardUser {
        const copy: BoardUser = Object.assign({}, boardUser);
        return copy;
    }
}
